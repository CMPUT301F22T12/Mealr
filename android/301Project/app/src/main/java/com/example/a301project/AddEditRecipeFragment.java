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

public class AddEditRecipeFragment extends DialogFragment {
    private Spinner categoryName;
    private EditText comments;
    private EditText title;
    private EditText servings;
    private EditText prepTime;
    private AddEditRecipeFragment.OnFragmentInteractionListener listener;
    private Button deleteButton;

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

    Recipe currentRecipe;
    boolean createNewRecipe;

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


        // Category spinner
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.category_array, R.layout.ingredient_unit_item);
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
