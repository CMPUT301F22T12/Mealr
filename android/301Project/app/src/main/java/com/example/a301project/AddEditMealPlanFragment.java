package com.example.a301project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Calendar;
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
    private DatePickerDialog.OnDateSetListener startdateSetListener;
    private DatePickerDialog.OnDateSetListener enddateSetListener;
    private EditText startDate;
    private EditText endDate;
    private EditText mealTitle;
    private AutoCompleteTextView ingredientAutoText;
    private AutoCompleteTextView recipeAutoText;
    private ArrayAdapter<Ingredient> ingredientArrayAdapter;
    private ArrayAdapter<Recipe> recipeArrayAdapter;
    private ArrayList<Ingredient> ingredientsDataList;
    private ArrayList<Recipe> recipesDataList;
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
     * Method to clear recipe,
     * Resets the internal meal plan list the new one.
     *
     * @param r {@link ArrayList} list of recipes to set the data list to
     */
    private void setRecipeDataList(ArrayList<Recipe> r) {
        recipeAutoCompleteList.clear();
        for (Recipe i : r) {
            if (recipesDataList.stream().noneMatch(i1 -> i.getTitle().equals(i1.getTitle()))) {
                recipeAutoCompleteList.add(i.getTitle());
            }
        }
        recipeAutoCompleteAdapter.notifyDataSetChanged();
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

        // get the current date as the default in date picker
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

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
        mealTitle = view.findViewById(R.id.edit_title_mealplan);
        ingredientAutoText = view.findViewById(R.id.autoCompleteIngredient_mealplan);
        recipeAutoText = view.findViewById(R.id.autoCompleteRecipe_mealplan);

        // if tag is ADD, hide delete button
        String title;
        if (this.getTag().equals("ADD")) {
            title = "Add Entry";
            deleteMealButton.setVisibility(View.GONE);
        } else {
            title = "Edit Entry";
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
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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

        // Load autocomplete recipes
        recipeController.getRecipes(res -> setRecipeDataList(res));
        recipeAutoCompleteAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, recipeAutoCompleteList);
        recipeAutoText.setAdapter(recipeAutoCompleteAdapter);

        // Set a HARDCODED delay to make sure the keyboard is up first and the dropdown
        // appears above not behind the keyboard
        recipeAutoText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(() -> recipeAutoText.showDropDown());
                        }
                    }, DELAY);
                }
            }
        });

        recipeAutoText.setOnTouchListener((v, event) -> {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(() -> recipeAutoText.showDropDown());
                }
            }, DELAY);
            return false;
        });

        // Load recipes
        recipesDataList = new ArrayList<>();
        recipesDataList.addAll(currentMealPlan.getRecipes());
        recipeArrayAdapter = new MealPlanRecipeListAdapter(getContext(), recipesDataList);
        recipeList.setAdapter(recipeArrayAdapter);

        addRecipeButton.setOnClickListener(view_ -> {
            String recipeName = recipeAutoText.getText().toString();
            if (!recipeName.isEmpty()) {
                recipesDataList.add(0, new Recipe(recipeName, (long) 1.0));
                recipeArrayAdapter.notifyDataSetChanged();
                recipeAutoText.setText("");

                // Hide the keyboard now
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view_.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                // Remove from autocomplete for future
                if (recipeAutoCompleteList.contains(recipeName)) {
                    recipeAutoCompleteList.remove(recipeName);

                    // Idk why but we have to create a new one for this to work
                    recipeAutoCompleteAdapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_dropdown_item_1line, recipeAutoCompleteList);
                    recipeAutoText.setAdapter(recipeAutoCompleteAdapter);
                }
            }
        });

        // set the date pickers for start date
        startDate.setOnClickListener(new View.OnClickListener() {
            /**
             * Method invoked when the view is clicked
             * shows date picker
             *
             * @param view {@link View} the view that contains the selected date
             */
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker = new DatePickerDialog(
                        getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, startdateSetListener, year, month, day);
                datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePicker.show();
            }
        });

        startdateSetListener = new DatePickerDialog.OnDateSetListener() {
            /**
             * Method invoked a date is selected
             * sets the selected date as the best before date for this ingredient
             *
             * @param datePicker {@link DatePicker} the date picker in view
             * @param year       {@link Integer}  the year selected
             * @param month      {@link Integer} the month selected
             * @param dayOfMonth {@link Integer} the day selected
             */
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1;
                String startdate = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", dayOfMonth);
                startDate.setText(startdate);
            }
        };

        // set the date pickers for end date
        endDate.setOnClickListener(new View.OnClickListener() {
            /**
             * Method invoked when the view is clicked
             * shows date picker
             *
             * @param view {@link View} the view that contains the selected date
             */
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker = new DatePickerDialog(
                        getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, enddateSetListener, year, month, day);
                datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePicker.show();
            }
        });

        enddateSetListener = new DatePickerDialog.OnDateSetListener() {
            /**
             * Method invoked a date is selected
             * sets the selected date as the best before date for this ingredient
             *
             * @param datePicker {@link DatePicker} the date picker in view
             * @param year       {@link Integer}  the year selected
             * @param month      {@link Integer} the month selected
             * @param dayOfMonth {@link Integer} the day selected
             */
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1;
                String enddate = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", dayOfMonth);
                endDate.setText(enddate);
            }
        };

        // set the text boxes for when tag is EDIT
        mealTitle.setText(currentMealPlan.getName());
        startDate.setText(currentMealPlan.getStartDate());
        endDate.setText(currentMealPlan.getEndDate());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    /**
                     * Method for getting and setting attributes of current recipe
                     *
                     * @param dialogInterface {@link DialogInterface} the dialog interface of this fragment
                     * @param i               {@link Integer} ID of the selected item
                     */
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                AlertDialog alertDialog = (AlertDialog) dialog;
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mealTitle = AddEditMealPlanFragment.this.mealTitle.getText().toString();
                        String startDate = AddEditMealPlanFragment.this.startDate.getText().toString();
                        String endDate = AddEditMealPlanFragment.this.endDate.getText().toString();


                        // check if any field is empty
                        // if empty, reject add and show missing fields
                        boolean hasEmpty = startDate.isEmpty() || endDate.isEmpty() || ingredientsDataList.stream().anyMatch(i_ -> i_.getName().isEmpty() || i_.getAmount().isNaN());

                        if (mealTitle.isEmpty()) {
                            AddEditMealPlanFragment.this.mealTitle.setError("Can't be empty");
                        }
                        if (startDate.isEmpty()) {
                            AddEditMealPlanFragment.this.startDate.setError("Can't be empty");
                        }
                        if (endDate.isEmpty()) {
                            AddEditMealPlanFragment.this.endDate.setError("Can't be empty");
                        }

                        if (hasEmpty) {
                            return;
                        }

                        currentMealPlan.setName(mealTitle);
                        currentMealPlan.setStartDate(startDate);
                        currentMealPlan.setEndDate(endDate);
                        currentMealPlan.setIngredients(ingredientsDataList);
                        currentMealPlan.setRecipes(recipesDataList);

                        listener.onConfirmPressed(currentMealPlan, createNewMeal);
                        dialog.dismiss();
                    }
                });
            }
        });
        return dialog;
    }

    /**
     * Method to create a new AddEditRecipe fragment
     * @param mealplan {@link MealPlan} the current recipe
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
