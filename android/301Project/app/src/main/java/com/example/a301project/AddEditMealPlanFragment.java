package com.example.a301project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
            currentMealPlan = (MealPlan) bundle.get("recipe");
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
                builder.setMessage("Are you sure you want to delete this Meal?")
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
    }
}
