package com.example.a301project;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Collections;

/**
 * /**
 *  Main Activity class for Recipes
 *  functionalities for add, edit, delete
 *  initiates an RecipeController object that has access to firebase data
 *  handles sorting of recipes
 *  @return void
 */
public class RecipeActivity extends NavActivity implements AddEditRecipeFragment.OnFragmentInteractionListener {
    private ListView listView;
    private ArrayAdapter<Recipe> recipeArrayAdapter;
    private ArrayList<Recipe> recipeDataList = new ArrayList<>();
    private RecipeController controller = new RecipeController();
    private final String[] sortOptions = {"Title", "Prep Time", "Servings", "Category"};
    private Spinner sortSpinner;
    private Switch sortSwitch;
    Button addButton;
    public int position = -1;

    /**
     * Method for the activity becomes active and can receive input
     * Navigation panel finds the menu and displays it
     */
    @Override
    protected void onResume() {
        super.onResume();
        bottomNav.getMenu().findItem(R.id.action_recipes).setChecked(true);
    }

    /**
     * Method for initializing attributes of this activity
     * @param savedInstanceState {@link Bundle} the last saved instance of the fragment, NULL if newly created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // We have to put our layout in the space for the content
        ViewGroup content = findViewById(R.id.nav_content);
        getLayoutInflater().inflate(R.layout.activity_recipe, content, true);
        addButton = findViewById(R.id.add_recipe_button);
        // Set the correct button to be selected
        bottomNav.getMenu().findItem(R.id.action_recipes).setChecked(true);
        // Fetch the data
        controller.getRecipes(res -> setRecipeDataList(res));

        // Attach to listView
        recipeArrayAdapter = new RecipeListAdapter(this, recipeDataList);
        listView = findViewById(R.id.recipeListView);
        listView.setAdapter(recipeArrayAdapter);

        // Setup sorting
        // sort by title, prep time, servings, category
        sortSpinner = findViewById(R.id.recipeSortSpinner);
        sortSwitch = findViewById(R.id.recipeSortSwitch);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, sortOptions);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Method invoked when a sort parameter in this view is selected
             * @param adapterView {@link AdapterView} the AdapterView where the selection happened
             * @param view {@link View} the view that was clicked
             * @param i {@link Integer} position of the view in the adapter
             * @param l {@link Long} the row ID of the item that was selected
             */
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortDataBySpinner();
            }

            /**
             * Method for when no spinner item is selected
             * @param adapterView {@link AdapterView} the AdapterView where the selection happened
             * @return void
             */
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // nothing happens

            }
        });
        recipeArrayAdapter.notifyDataSetChanged();
        addButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Method invoked when the add button is clicked
             * shows Add Recipe fragment
             * @param view {@link View} the view that contains the add button
             */
            @Override
            public void onClick(View view) {
                Recipe newRecipe = new Recipe("","","","", 0L, 0L,new ArrayList<>());
                AddEditRecipeFragment.newInstance(newRecipe,true).show(getSupportFragmentManager(),"ADD");
            }
        });
        sortSwitch.setChecked(true);
        sortSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /**
             * Method for when sort switch is clicked
             * @param compoundButton {@link CompoundButton} the switch button view that has changed
             * @param b {@link boolean} the checked state of the button
             * @return void
             */
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // sortData determines what parameter to sort the Recipes by
                sortDataBySpinner();
            }
        });
        sortDataBySpinner();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Method invoked when an item in this view is clicked
             * @param adapterView {@link AdapterView} the AdapterView where the selection happened
             * @param view {@link View} the view that was clicked
             * @param i {@link Integer} position of the view in the adapter
             * @param l {@link Long} the row ID of the item that was selected
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // when a Recipe item is clicked, open Edit Recipe fragment
                position = i;
                Recipe selected = (Recipe) adapterView.getItemAtPosition(i);
                AddEditRecipeFragment.newInstance(recipeArrayAdapter.getItem(position), false).show(getSupportFragmentManager(), "EDIT");
            }
        });

    }

    /**
     * Method to clear recipeDataList,
     * Resets the internal recipe list the new one.
     * @param r {@link ArrayList} list of recipes to set the data list to
     */
    private void setRecipeDataList(ArrayList<Recipe> r) {
        recipeDataList.clear();
        recipeDataList.addAll(r);
        recipeArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Sorts internal recipe list by selected parameters defined the sortOptions attribute
     * Sort By parameters: Title, Prep Time, Servings, Category
     */
    private void sortDataBySpinner() {
        // Make sure views are defined
        sortSpinner = findViewById(R.id.recipeSortSpinner);
        sortSwitch = findViewById(R.id.recipeSortSwitch);

        String sortBy = sortSpinner.getSelectedItem().toString();
        Integer asc = sortSwitch.isChecked() ? 1 : -1;

        // determine which sort option was selected, then sort them in ascending or descending order
        // ascending or descending is based on the asc variable
        Collections.sort(recipeDataList, (Recipe r1, Recipe r2) -> {
                    if (sortBy.equals(sortOptions[0])) {
                        return asc * r1.getTitle().toLowerCase().compareTo(r2.getTitle().toLowerCase());
                    } else if (sortBy.equals(sortOptions[1])) {
                        return asc * r1.getPrepTime().compareTo(r2.getPrepTime());
                    } else if (sortBy.equals(sortOptions[2])) {
                        return asc * r1.getServings().compareTo(r2.getServings());
                    } else {
                        return asc * r1.getCategory().toLowerCase().compareTo(r2.getCategory().toLowerCase());
                    }
                }
        );

        recipeArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Method for adding recipes
     * trigger when Add button clicked
     * @param recipe {@link Recipe} a Recipe to be added
     * @return void
     */
    public void addRecipe(Recipe recipe) {
        recipeArrayAdapter.add(recipe);
        controller.addRecipe(recipe);
    }

    /**
     * Method invoked when Add/Edit fragment confirm button clicked
     * @param recipe {@link Recipe}
     * @param createNewRecipe {@link boolean}
     * checks whether to create new recipe or to update existing
     * @return void
     */
    @Override
    public void onConfirmPressed(Recipe recipe, boolean createNewRecipe) {
        if (createNewRecipe) {
            addRecipe(recipe);
        }
        else {
            controller.notifyUpdate(recipe);
        }
        recipeArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Method for deleting a recipe when confirm is clicked
     * @param currentRecipe {@link Recipe} the current recipe to be deleted
     */
    @Override
    public void onDeleteConfirmed(Recipe currentRecipe) {
        recipeArrayAdapter.remove(currentRecipe);
    }
}