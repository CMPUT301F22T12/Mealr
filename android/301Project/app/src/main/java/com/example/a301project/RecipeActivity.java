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
 * Renders the recipes for the user and allows them to modify or add them
 */
public class RecipeActivity extends NavActivity implements AddEditRecipeFragment.OnFragmentInteractionListener {
    private ListView listView;
    private ArrayAdapter<Recipe> recipeArrayAdapter;
    private ArrayList<Recipe> recipeDataList = new ArrayList<>();
    private RecipeController controller = new RecipeController();
    final String[] sortOptions = {"Title", "Prep Time", "Servings", "Category"};
    private Spinner sortSpinner;
    private Switch sortSwitch;
    Button addButton;
    public int position = -1;

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
        sortSpinner = findViewById(R.id.recipeSortSpinner);
        sortSwitch = findViewById(R.id.recipeSortSwitch);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, sortOptions);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortDataBySpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe newRecipe = new Recipe("","","","", 0L, 0L,new ArrayList<>());
                AddEditRecipeFragment.newInstance(newRecipe,true).show(getSupportFragmentManager(),"ADD");
            }
        });
        sortSwitch.setChecked(true);
        sortSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sortDataBySpinner();
            }
        });
        sortDataBySpinner();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                Recipe selected = (Recipe) adapterView.getItemAtPosition(i);
                AddEditRecipeFragment.newInstance(recipeArrayAdapter.getItem(position), false).show(getSupportFragmentManager(), "EDIT");
            }
        });

    }

    /**
     * Sets the internal recipe list the new one.
     *
     * @param a ArrayList of recipes to set the data list to
     */
    private void setRecipeDataList(ArrayList<Recipe> a) {
        recipeDataList.clear();
        recipeDataList.addAll(a);
        recipeArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Sorts internal recipe list by selected filters defined the sortOptions attribute
     */
    private void sortDataBySpinner() {
        // Make sure views are defined
        sortSpinner = findViewById(R.id.recipeSortSpinner);
        sortSwitch = findViewById(R.id.recipeSortSwitch);

        String sortBy = sortSpinner.getSelectedItem().toString();
        Integer asc = sortSwitch.isChecked() ? 1 : -1;

        Collections.sort(recipeDataList, (Recipe r1, Recipe r2) -> {
                    if (sortBy.equals(sortOptions[0])) {
                        return asc * r1.getTitle().compareTo(r2.getTitle());
                    } else if (sortBy.equals(sortOptions[1])) {
                        return asc * r1.getPrepTime().compareTo(r2.getPrepTime());
                    } else if (sortBy.equals(sortOptions[2])) {
                        return asc * r1.getServings().compareTo(r2.getServings());
                    } else {
                        return asc * r1.getCategory().compareTo(r2.getCategory());
                    }
                }
        );

        recipeArrayAdapter.notifyDataSetChanged();
    }

    public void addRecipe(Recipe recipe) {
        recipeArrayAdapter.add(recipe);
        controller.addRecipe(recipe);
    }

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
}