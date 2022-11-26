package com.example.a301project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A class for a fragment that handles adding and editing recipes
 * Fragment is activated when user clicks certain buttons
 */
@SuppressWarnings({"SpellCheckingInspection", "unchecked"})
public class AddEditRecipeFragment extends DialogFragment {
    private Spinner categoryName;
    private EditText comments;
    private EditText title;
    private EditText servings;
    private EditText prepTime;
    private AddEditRecipeFragment.OnFragmentInteractionListener listener;
    private Recipe currentRecipe;
    private boolean createNewRecipe;
    private ImageView image;
    private Button uploadButton;
    private Button cameraButton;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private String photoUrl;
    private ArrayAdapter<Ingredient> ingredientArrayAdapter;
    private ArrayList<Ingredient> ingredientsDataList;
    private AutoCompleteTextView ingredientAutoText;
    private final IngredientController ingredientController = new IngredientController();
    private final ArrayList<String> ingredientAutoCompleteList = new ArrayList<>();
    private ArrayAdapter<String> ingredientAutoCompleteAdapter;
    private ArrayList<CharSequence> categoryOptions;
    private AddEditRecipeController addEditRecipeController;
    private Resources res;

    /**
     * Method that responds when the fragment has been interacted with
     * OnConfirmPressed either creates a new Recipe or updates an existing one based on boolean createNewRecipe
     * onDeleteConfirmed deletes the current recipe
     */
    public interface OnFragmentInteractionListener {
        void onConfirmPressed(Recipe currentRecipe, boolean createNewRecipe);
        void onDeleteConfirmed(Recipe currentRecipe);
    }

    /**
     * Method to clear ingredient,
     * Resets the internal recipe list the new one.
     * @param r {@link ArrayList} list of recipes to set the data list to
     */
    private void setIngredientDataList(ArrayList<Ingredient> r) {
        ingredientAutoCompleteList.clear();
        for (Ingredient i : r) {
            if (ingredientsDataList.stream().noneMatch(i1 -> i.getName().equals(i1.getName()))) {
                ingredientAutoCompleteList.add(i.getName());
            }
        }
        ingredientAutoCompleteAdapter.notifyDataSetChanged();
    }

    /**
     * Saves the bitmap to firebase storage with the name of the recipe
     * @param bitmap {@link Bitmap} the bitmap to save
     * Shows toast based on result
     */
    private void saveBitmapToFirebase(Bitmap bitmap) {
        uploadButton.setEnabled(false);
        cameraButton.setEnabled(false);
        StorageReference storageRef = storage.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user.getEmail() != null;
        StorageReference imagesRef = storageRef.child("mealImages").child(user.getEmail()).child(title.getText().toString());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(bytes);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getContext(), "An error occurred!", Toast.LENGTH_LONG).show();
                uploadButton.setEnabled(true);
                cameraButton.setEnabled(true);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(), "Image uploaded!", Toast.LENGTH_LONG).show();
                image.setImageBitmap(bitmap);
                image.setClipToOutline(true);

                Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        photoUrl = uri.toString();
                    }
                });
                uploadButton.setEnabled(true);
                cameraButton.setEnabled(true);
            }
        });
    }

    /**
     * Method to set the fragment attributes
     * Sets the information of current Recipe if the tag is EDIT
     * Sets empty EditText views if the tag is ADD, and hides delete button
     * Creates new Recipe or resets information of current recipe based on the tag
     * @param savedInstanceState {@link Bundle} the last saved instance state of fragment, NULL if
     *                                         fragment is newly created
     * @return dialog fragment with the appropriate fields
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_edit_recipe_layout, null);

        Bundle bundle = getArguments();
        if (bundle != null) {
            currentRecipe = (Recipe) bundle.get("recipe");
            createNewRecipe = (boolean) bundle.get("createNew");
        }
        // populate the text boxes with information of the selected recipe
        // empty if ADD
        categoryName = view.findViewById(R.id.edit_category_recipe);
        comments = view.findViewById(R.id.edit_comments);
        title = view.findViewById(R.id.edit_title);
        servings = view.findViewById(R.id.edit_servings);
        prepTime = view.findViewById(R.id.edit_prep_time);
        Button deleteButton = view.findViewById(R.id.delete_recipe_button);
        image = view.findViewById(R.id.recipeImageView);
        uploadButton = view.findViewById(R.id.uploadImageButton);
        cameraButton = view.findViewById(R.id.cameraButton);
        ListView ingredientListView = view.findViewById(R.id.recipe_ingredients_listview);
        ingredientAutoText = view.findViewById(R.id.autoCompleteIngredient);
        Button ingredientsAddButton = view.findViewById(R.id.add_ingredient_button);

        // initialize the category spinner and the FireBase controller
        addEditRecipeController = new AddEditRecipeController();
        DocumentReference documentReference = addEditRecipeController.getDocumentReference();
        categoryOptions = new ArrayList<>();
        res = getActivity().getResources();

        // max length of 10 characters
        InputFilter[] filterArray = new InputFilter[2];
        filterArray[0] = new InputFilter.LengthFilter(10);
        // only a-zA-Z and spaces
        filterArray[1] = (source, start, end, dest, dstart, dend) -> {
            if (source.equals("")) {
                return source;
            }
            if (source.toString().matches("[a-zA-Z ]+")) {
                return source;
            }
            return source.subSequence(start, end-1);
        };

        // if tag is ADD, hide delete button
        if (this.getTag().equals("ADD")) {
            deleteButton.setVisibility(View.GONE);
        }
        // OnClickListener for delete button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Method for when Delete button is clicked
             * Another fragment pops up to confirm whether user meant to delete
             * @param view {@link View} the view of the fragment that was clicked
             */
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to delete this Recipe?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            /**
                             * Method for when positive button clicked in delete fragment
                             * @param dialog {@link DialogInterface} the dialog that received the click
                             * @param id {@link Integer} ID of the button that was clicked
                             */
                            public void onClick(DialogInterface dialog, int id) {
                                // updates firebase by removing current recipe
                                RecipeController controller = new RecipeController();
                                controller.removeRecipe(currentRecipe);
                                listener.onDeleteConfirmed(currentRecipe);
                                Fragment frag = getParentFragmentManager().findFragmentByTag("EDIT");
                                getParentFragmentManager().beginTransaction().remove(frag).commit();
                                Toast.makeText(getContext(), "Recipe Delete Successful", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            /**
                             * Method for when negative button is clicked in delete fragment
                             * @param dialog {@link DialogInterface} the interface of this pop up fragment
                             * @param id {@link Integer} ID of the recipe to be deleted
                             */
                            public void onClick(DialogInterface dialog, int id) {
                                // returns to Edit fragment
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


        // Create a launcher for the gallery upload intent
        ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri uri = data.getData();
                            try {
                                // Get the images as a bitmap from the result
                                InputStream in = getActivity().getContentResolver().openInputStream(uri);
                                final Bitmap bitmap = BitmapFactory.decodeStream(in);

                                // Save the image to firebase
                                title = view.findViewById(R.id.edit_title);
                                if (!title.getText().toString().isEmpty()) {
                                    saveBitmapToFirebase(bitmap);
                                }
                            } catch (FileNotFoundException e) {
                                Toast.makeText(getContext(), "An error occurred!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

        // Create a launcher for the camera upload intent
        ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    try {
                        Intent data = result.getData();
                        if (data != null) {
                            final Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                            saveBitmapToFirebase(bitmap);
                        }
                        } catch(Exception e){
                        Toast.makeText(getContext(), "An error occurred!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraActivityResultLauncher.launch(i);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryActivityResultLauncher.launch(i);
            }
        });


        // Create an adapter for the category spinner
        ArrayAdapter<CharSequence> categoryAdapter = new ArrayAdapter<>(this.getContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, categoryOptions);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryName.setAdapter(categoryAdapter);
        categoryName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Method invoked when an item in this view has been selected
             * @param adapterView {@link AdapterView} the AdapterView where the selection happened
             * @param view {@link View} the view that was clicked
             * @param i {@link Integer} position of the view in the adapter
             * @param l {@link Long} the row ID of the item that was selected
             */
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (categoryAdapter.getItem(i).equals("Add Category")) {

                    // create the edit text and set constraints
                    EditText customCategory = new EditText(getContext());
                    customCategory.setHint("Only letters allowed");
                    customCategory.setFilters(filterArray);

                    // build the alert dialog -> which will prompt the user to enter a new category
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setView(customCategory);
                    builder.setMessage("Enter custom category")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();

                                    // set the category back to the what it was before
                                    categoryName.setSelection(categoryAdapter.getPosition(currentRecipe.getCategory()));
                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        /**
                         * When the 'OK' button is clicked on the Alert Dialog which prompts user to enter a new category
                         * @param v: The {@link View} - which is the 'OK' button
                         */
                        @Override
                        public void onClick(View v) {
                            // get the user input -> and check that it is not empty
                            String newCategory = customCategory.getText().toString().trim();
                            if (newCategory.isEmpty()) {
                                customCategory.setError("Can't be empty");
                                return;
                            }

                            // if the category is not empty -> check if it already exist
                            Iterator<CharSequence> listIterator = categoryOptions.iterator();
                            boolean exists = false;
                            while (listIterator.hasNext()) {
                                String nextValue = listIterator.next().toString();
                                if (nextValue.equalsIgnoreCase(newCategory)) {
                                    exists = true;
                                    newCategory = nextValue;
                                }
                            }

                            // if the category doesn't already exist -> add the data
                            if (!exists) {
                                int size = categoryOptions.size();
                                categoryOptions.add(size-1, newCategory);
                                categoryAdapter.notifyDataSetChanged();

                                // add the data to firebase
                                addEditRecipeController.addRecipeCategory(newCategory);
                            }

                            // select the spinner value
                            int j = categoryAdapter.getPosition(newCategory);
                            categoryName.setSelection(j);
                            currentRecipe.setCategory(newCategory);

                            // close the dialog
                            dialog.dismiss();
                        }
                    });

                }
                else {
                    // user didn't select the add custom option
                    currentRecipe.setCategory(categoryAdapter.getItem(i).toString());
                }
            }

            /**
             * Method invoked when nothing is selected
             * selection disappears from the view
             * @param adapterView {@link AdapterView} the AdapterView that contains no selected item
             */
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // nothing happens, the spinner goes away if you click away from the spinner
            }
        });

        categoryName.setSelection(categoryAdapter.getPosition(currentRecipe.getCategory()));
        comments.setText(currentRecipe.getComments());
        title.setText(currentRecipe.getTitle());
        if(!createNewRecipe) {
            servings.setText(String.valueOf(currentRecipe.getServings()));
            prepTime.setText(String.valueOf(currentRecipe.getPrepTime()));
        }
        // If we have a photo already, load it in
        if (currentRecipe.getPhoto() != null && !currentRecipe.getPhoto().isEmpty()) {
            Picasso.get().load(currentRecipe.getPhoto()).into(image);
            image.setClipToOutline(true);
            photoUrl = currentRecipe.getPhoto();
        }

        // Load autocomplete ingredients
        ingredientController.getIngredients(res -> setIngredientDataList(res));
        ingredientAutoCompleteAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, ingredientAutoCompleteList);
        ingredientAutoText.setAdapter(ingredientAutoCompleteAdapter);

        // Set a HARDCODED delay to make sure the keyboard is up first and the dropdown
        // appears above not behind the keyboard
        int DELAY = 500;
        ingredientAutoText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(() -> ingredientAutoText.showDropDown());
                        }
                    }, DELAY);
                }
            }
        });

        ingredientAutoText.setOnTouchListener((v, event) -> {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(() -> ingredientAutoText.showDropDown());
                }
            }, DELAY);
            return false;
        });

        // Load ingredients
        ingredientsDataList = new ArrayList<>();
        ingredientsDataList.addAll(currentRecipe.getIngredients());
        ingredientArrayAdapter = new RecipeIngredientListAdapter(getContext(), ingredientsDataList);
        ingredientListView.setAdapter(ingredientArrayAdapter);

        ingredientsAddButton.setOnClickListener(view_ -> {
            String ingredientName = ingredientAutoText.getText().toString();
            if (!ingredientName.isEmpty()) {
                ingredientsDataList.add(0, new Ingredient(ingredientName, 1));
                ingredientArrayAdapter.notifyDataSetChanged();
                ingredientAutoText.setText("");

                // Hide the keyboard now
                InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view_.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                // Remove from autocomplete for future
                if (ingredientAutoCompleteList.contains(ingredientName)) {
                    ingredientAutoCompleteList.remove(ingredientName);

                    // Idk why but we have to create a new one for this to work
                    ingredientAutoCompleteAdapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_dropdown_item_1line, ingredientAutoCompleteList);
                    ingredientAutoText.setAdapter(ingredientAutoCompleteAdapter);
                }
            }
        });

        // gets the spinner value from firebase
        Task<DocumentSnapshot> documentSnapshot = documentReference.get();
        documentSnapshot.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            /**
             * When the documentSnapshot has been successfully accessed from Firebase
             * @param task the {@link Task<DocumentSnapshot>}
             */
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> result = task.getResult().getData();

                    // add the default spinner values and get the custom categories from firebase (if they exist)
                    List<CharSequence> defaultCategories = List.of(res.getStringArray(R.array.category_array_recipe));
                    categoryOptions.addAll(defaultCategories);
                    if (result != null && result.containsKey("RecipeCategories")) {
                        categoryOptions.addAll(categoryOptions.size()-1,(ArrayList<CharSequence>) result.get("RecipeCategories"));
                    }
                    categoryAdapter.notifyDataSetChanged();

                    // set the category spinner at the correct value for the current recipe
                    categoryName.setSelection(categoryAdapter.getPosition(currentRecipe.getCategory()));
                }
            }
        });

        // create a new builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setView(view)
                .setTitle("Add/Edit Recipe")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog dialog = builder.create();

        // modify the positive button so it doesn't close automatically if there are errors
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                AlertDialog alertDialog = (AlertDialog) dialog;
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    /**
                     * Add functionality to the 'Confirm' button
                     * @param v The View (in this case the 'Confirm' button)
                     */
                    @Override
                    public void onClick(View v) {
                        String title = AddEditRecipeFragment.this.title.getText().toString();
                        String comments = AddEditRecipeFragment.this.comments.getText().toString();
                        String servings = AddEditRecipeFragment.this.servings.getText().toString();
                        String prepTime = AddEditRecipeFragment.this.prepTime.getText().toString();


                        // check if any field is empty
                        boolean hasEmpty = title.isEmpty() || servings.isEmpty() || prepTime.isEmpty() || ingredientsDataList.stream().anyMatch(i_ -> i_.getName().isEmpty() || i_.getAmount().isNaN());

                        // show errors
                        if(title.isEmpty()) {
                            AddEditRecipeFragment.this.title.setError("Can't be empty");
                        }
                        if(servings.isEmpty()) {
                            AddEditRecipeFragment.this.servings.setError("Can't be empty");
                        }
                        if(prepTime.isEmpty()) {
                            AddEditRecipeFragment.this.prepTime.setError("Can't be empty");
                        }
                        if (hasEmpty) {
                            return;
                        }
                        
                        Long Servings = Long.valueOf(servings);
                        Long PrepTime = Long.valueOf(prepTime);

                        currentRecipe.setTitle(title);
                        currentRecipe.setComments(comments);
                        currentRecipe.setServings(Servings);
                        currentRecipe.setPrepTime(PrepTime);

                        if (photoUrl != null && !photoUrl.isEmpty()) {
                            currentRecipe.setPhoto(photoUrl);
                        }
                        currentRecipe.setIngredients(ingredientsDataList);

                        listener.onConfirmPressed(currentRecipe, createNewRecipe);
                        dialog.dismiss();
                    }
                });
            }
        });
        return dialog;
    }

    /**
     * Method to create a new AddEditRecipe fragment
     * @param recipe {@link Recipe} the current recipe
     * @param createNew {@link boolean} variable that indicates whether to create a new recipe
     * @return An Add/Edit Recipe fragment
     */
    static AddEditRecipeFragment newInstance(Recipe recipe, boolean createNew , OnFragmentInteractionListener listener) {
        Bundle args = new Bundle();
        args.putSerializable("recipe",recipe);
        args.putSerializable("createNew", createNew);

        AddEditRecipeFragment fragment = new AddEditRecipeFragment();
        fragment.setArguments(args);
        fragment.listener = listener;

        return fragment;
    }
}
