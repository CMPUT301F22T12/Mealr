package com.example.a301project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

/**
 * A class for a fragment that handles adding and editing recipes
 * Fragment is activated when user clicks certain buttons
 */
public class AddEditRecipeFragment extends DialogFragment {
    private Spinner categoryName;
    private EditText comments;
    private EditText title;
    private EditText servings;
    private EditText prepTime;
    private AddEditRecipeFragment.OnFragmentInteractionListener listener;
    private Button deleteButton;

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
     * Method for when fragment is attached to the screen
     * @param context {@link Context} the context of the fragment
     * sets the listener object as OnFragmentInteractionListener
     * throws exception if context is not an instance of OnFragmentInteractionListener
     */
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

    Recipe currentRecipe;
    boolean createNewRecipe;

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
        // populate the textboxes with information of the selected recipe
        // empty if ADD
        categoryName = view.findViewById(R.id.edit_category_recipe);
        comments = view.findViewById(R.id.edit_comments);
        title = view.findViewById(R.id.edit_title);
        servings = view.findViewById(R.id.edit_servings);
        prepTime = view.findViewById(R.id.edit_prep_time);
        deleteButton = view.findViewById(R.id.delete_recipe_button);

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
                                Fragment frag = getActivity().getSupportFragmentManager().findFragmentByTag("EDIT");
                                getActivity().getSupportFragmentManager().beginTransaction().remove(frag).commit();
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


        // Category spinner
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.category_array_recipe, R.layout.ingredient_unit_item);
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
                // set the category of current recipe
                categoryName.setSelection(i);
                currentRecipe.setCategory(adapterView.getItemAtPosition(i).toString());
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
        servings.setText(String.valueOf(currentRecipe.getServings()));
        prepTime.setText(String.valueOf(currentRecipe.getPrepTime()));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add/Edit Recipe")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    /**
                     * Method for getting and setting attributes of current recipe
                     * @param dialogInterface {@link DialogInterface} the dialog interface of this fragment
                     * @param i {@link Integer} ID of the selected item
                     */
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = AddEditRecipeFragment.this.title.getText().toString();
                        String comments = AddEditRecipeFragment.this.comments.getText().toString();
                        String servings = AddEditRecipeFragment.this.servings.getText().toString();
                        String prepTime = AddEditRecipeFragment.this.prepTime.getText().toString();

                        Long longServings = Long.valueOf(servings);
                        Long longPrepTime = Long.valueOf(prepTime);

                        // check if any field is empty
                        // if empty, reject add
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

    /**
     * Method to create a new AddEditRecipe fragment
     * @param recipe {@link Recipe} the current recipe
     * @param createNew {@link boolean} variable that indicates whether to create a new recipe
     * @return An Add/Edit Recipe fragment
     */
    static AddEditRecipeFragment newInstance(Recipe recipe, boolean createNew) {
        Bundle args = new Bundle();
        args.putSerializable("recipe",recipe);
        args.putSerializable("createNew", createNew);

        AddEditRecipeFragment fragment = new AddEditRecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
