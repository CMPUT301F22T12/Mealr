package com.example.a301project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AddEditMealPlanFragment extends DialogFragment {
    private ListView ingredientList;
    private Button addIngredientButton;
    private ListView recipeList;
    private Button addRecipeButton;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Button deleteMealButton;
    private MealPlan currentMealPlan;
    private boolean createNewMeal;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private EditText startDate;
    private EditText endDate;
    private AutoCompleteTextView ingredientAutoText;
    private AutoCompleteTextView recipeAutoText;
    private ArrayAdapter<Ingredient> ingredientArrayAdapter;
    private ArrayList<Ingredient> ingredientsDataList;
    private IngredientController ingredientController = new IngredientController();
    private ArrayList<String> ingredientAutoCompleteList = new ArrayList<>();
    private ArrayAdapter<String> ingredientAutoCompleteAdapter;
    private RecipeController recipeController = new RecipeController();
    private ArrayList<String> recipeAutoCompleteList = new ArrayList<>();
    private ArrayAdapter<String> recipeAutoCompleteAdapter;
    private AddEditMealPlanFragment.OnFragmentInteractionListener listener;

    /**
     * Method that responds when the fragment has been interacted with
     * OnConfirmPressed either creates a new Meal Plan or updates an existing one based on boolean createNewMeal
     * onDeleteConfirmed deletes the current meal
     */
    public interface OnFragmentInteractionListener {
        void onConfirmPressed(MealPlan currentMealPlan, boolean createNewMeal);

        void onDeleteConfirmed(MealPlan currentMealPlan);

    }

    /**
     * Method to clear ingredient,
     * Resets the internal meal plan list the new one.
     *
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
     * Method to set the fragment attributes
     * Sets the information of current Recipe if the tag is EDIT
     * Sets empty EditText views if the tag is ADD, and hides delete button
     * Creates new Recipe or resets information of current recipe based on the tag
     *
     * @param savedInstanceState {@link Bundle} the last saved instance state of fragment, NULL if
     *                           fragment is newly created
     * @return dialog fragment with the appropriate fields
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_edit_mealplan_layout, null);

        Bundle bundle = getArguments();
        if (bundle != null) {
            currentMealPlan = (MealPlan) bundle.get("mealplan");
            createNewMeal = (boolean) bundle.get("createNew");
        }
        // populate text boxes with information on meal plan
        // empty if ADD
        deleteMealButton = view.findViewById(R.id.delete_mealplan_button);
        ingredientList = view.findViewById(R.id.mealplan_ingredients_listview);
        recipeList = view.findViewById(R.id.mealplan_recipes_listview);
        addIngredientButton = view.findViewById(R.id.add_ingredient_button_mealplan);
        addRecipeButton = view.findViewById(R.id.add_recipe_button_mealplan);
        startDate = view.findViewById(R.id.edit_start_date);
        endDate = view.findViewById(R.id.edit_end_date);
        ingredientAutoText = view.findViewById(R.id.autoCompleteIngredient_mealplan);
        recipeAutoText = view.findViewById(R.id.autoCompleteRecipe_mealplan);

        // if tag is ADD, hide delete button
        if (this.getTag().equals("ADD")) {
            deleteMealButton.setVisibility(View.GONE);
        }
        // OnClickListener for delete button
        deleteMealButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Method for when Delete button is clicked
             * Another fragment pops up to confirm whether user meant to delete
             *
             * @param view {@link View} the view of the fragment that was clicked
             */
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to delete this Meal Plan?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            /**
                             * Method for when positive button clicked in delete fragment
                             * @param dialog {@link DialogInterface} the dialog that received the click
                             * @param id {@link Integer} ID of the button that was clicked
                             */
                            public void onClick(DialogInterface dialog, int id) {
                                // updates firebase by removing current recipe
                                MealPlanController controller = new MealPlanController();
                                controller.removeMealPlan(currentMealPlan);
                                listener.onDeleteConfirmed(currentMealPlan);
                                Fragment frag = getParentFragmentManager().findFragmentByTag("EDIT");
                                getParentFragmentManager().beginTransaction().remove(frag).commit();
                                Toast.makeText(getContext(), "Meal Plan Delete Successful", Toast.LENGTH_LONG).show();
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
        ingredientsDataList.addAll(currentMealPlan.getIngredients());
        ingredientArrayAdapter = new RecipeIngredientListAdapter(getContext(), ingredientsDataList);
        ingredientList.setAdapter(ingredientArrayAdapter);

        addIngredientButton.setOnClickListener(view_ -> {
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add/Edit Meal Plan")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    /**
                     * Method for getting and setting attributes of current recipe
                     * @param dialogInterface {@link DialogInterface} the dialog interface of this fragment
                     * @param i {@link Integer} ID of the selected item
                     */
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String startDate = AddEditMealPlanFragment.this.startDate.getText().toString();
                        String endDate = AddEditMealPlanFragment.this.endDate.getText().toString();


                        // check if any field is empty
                        // if empty, reject add
                        boolean hasEmpty = startDate.isEmpty() || endDate.isEmpty() || ingredientsDataList.stream().anyMatch(i_ -> i_.getName().isEmpty() || i_.getAmount().isNaN());

                        if (hasEmpty) {
                            Toast.makeText(getContext(),  " Rejected: Missing Field(s)",Toast.LENGTH_LONG).show();
                            return;
                        }

                        currentMealPlan.setStartDate(startDate);
                        currentMealPlan.setEndDate(endDate);
                        currentMealPlan.setIngredients(ingredientsDataList);

                        listener.onConfirmPressed(currentMealPlan, createNewMeal);
                    }
                }).create();

    }
    /**
     * Method to create a new AddEditRecipe fragment
     * @param mealplan {@link Recipe} the current recipe
     * @param createNew {@link boolean} variable that indicates whether to create a new recipe
     * @return An Add/Edit Recipe fragment
     */
    static AddEditMealPlanFragment newInstance(MealPlan mealplan, boolean createNew , AddEditMealPlanFragment.OnFragmentInteractionListener listener) {
        Bundle args = new Bundle();
        args.putSerializable("mealplan",mealplan);
        args.putSerializable("createNew", createNew);

        AddEditMealPlanFragment fragment = new AddEditMealPlanFragment();
        fragment.setArguments(args);
        fragment.listener = listener;

        return fragment;
    }

}
