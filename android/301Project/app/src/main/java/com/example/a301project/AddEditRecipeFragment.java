package com.example.a301project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddEditRecipeFragment extends DialogFragment {
    private Spinner categoryName;
    private EditText comments;
    private EditText title;
    private EditText servings;
    private EditText prepTime;
    private AddEditRecipeFragment.OnFragmentInteractionListener listener;
    private Button deleteButton;
    private Recipe currentRecipe;
    private boolean createNewRecipe;
    private ImageView image;
    private Button uploadButton;


    public interface OnFragmentInteractionListener {
        void onConfirmPressed(Recipe currentRecipe, boolean createNewRecipe);
        void onDeleteConfirmed(Recipe currentRecipe);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddEditRecipeFragment.OnFragmentInteractionListener) {
            listener = (AddEditRecipeFragment.OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context + "must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_edit_recipe_layout, null);

        Bundle bundle = getArguments();
        if (bundle != null) {
            currentRecipe = (Recipe) bundle.get("recipe");
            createNewRecipe = (boolean) bundle.get("createNew");
        }
        categoryName = view.findViewById(R.id.edit_category_recipe);
        comments = view.findViewById(R.id.edit_comments);
        title = view.findViewById(R.id.edit_title);
        servings = view.findViewById(R.id.edit_servings);
        prepTime = view.findViewById(R.id.edit_prep_time);
        deleteButton = view.findViewById(R.id.delete_recipe_button);
        image = view.findViewById(R.id.recipeImageView);
        uploadButton = view.findViewById(R.id.uploadImageButton);

        if (this.getTag().equals("ADD")) {
            deleteButton.setVisibility(View.GONE);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to delete this Recipe?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                RecipeController controller = new RecipeController();
                                controller.removeRecipe(currentRecipe);
                                listener.onDeleteConfirmed(currentRecipe);
                                Fragment frag = getActivity().getSupportFragmentManager().findFragmentByTag("EDIT");
                                getActivity().getSupportFragmentManager().beginTransaction().remove(frag).commit();
                                Toast.makeText(getContext(), "Recipe Delete Successful", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null){
                                Uri uri = data.getData();
                                InputStream in;
                                try {
                                    in = getActivity().getContentResolver().openInputStream(uri);
                                    final Bitmap selected_img = BitmapFactory.decodeStream(in);
                                    image.setImageBitmap(selected_img);
                                    image.setClipToOutline(true);

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), "An error occurred!", Toast.LENGTH_LONG).show();
                                }
                            }

                        }
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




        // Category spinner
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.category_array_recipe, R.layout.ingredient_unit_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryName.setAdapter(categoryAdapter);
        categoryName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryName.setSelection(i);
                currentRecipe.setCategory(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        categoryName.setSelection(categoryAdapter.getPosition(currentRecipe.getCategory()));
        comments.setText(currentRecipe.getComments());
        title.setText(currentRecipe.getTitle());
        servings.setText(String.valueOf(currentRecipe.getServings()));
        prepTime.setText(String.valueOf(currentRecipe.getPrepTime()));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add/Edit Recipe")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = AddEditRecipeFragment.this.title.getText().toString();
                        String comments = AddEditRecipeFragment.this.comments.getText().toString();
                        String servings = AddEditRecipeFragment.this.servings.getText().toString();
                        String prepTime = AddEditRecipeFragment.this.prepTime.getText().toString();

                        Long longServings = Long.valueOf(servings);
                        Long longPrepTime = Long.valueOf(prepTime);

                        // check if any field is empty
                        boolean hasEmpty = title.isEmpty() || servings.isEmpty() || prepTime.isEmpty();

                        if (hasEmpty) {
                            Toast.makeText(getContext(),  " Rejected: Missing Field(s)",Toast.LENGTH_LONG).show();
                            return;
                        }

                        currentRecipe.setTitle(title);
                        currentRecipe.setComments(comments);
                        currentRecipe.setServings(longServings);
                        currentRecipe.setPrepTime(longPrepTime);

                        listener.onConfirmPressed(currentRecipe, createNewRecipe);

                    }
                }).create();

    }
    static AddEditRecipeFragment newInstance(Recipe recipe, boolean createNew) {
        Bundle args = new Bundle();
        args.putSerializable("recipe",recipe);
        args.putSerializable("createNew", createNew);

        AddEditRecipeFragment fragment = new AddEditRecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
