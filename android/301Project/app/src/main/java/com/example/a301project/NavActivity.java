package com.example.a301project;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;

/**
 * Wrapper class to add a navigation bar for other activities.
 * Add new buttons to layout/nav_items.xml
 */
public class NavActivity extends AppCompatActivity {
    protected BottomNavigationView bottomNav;

    /**
     * Method for on creating the activity
     * @param savedInstanceState {@link Bundle} the last saved instance of this activity, NULL if its newly created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        // sets the navigation bar at the bottom
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            /**
             * Method invoked when an item is selected in the navigation bar
             * @param item {@link MenuItem} the item selected from the menu
             * @return true
             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Retrieve the selected item ID to determine which activity to switch to
                // in future prototypes this will become fragments
                int id = item.getItemId();

                renderFragment(id);

                return true;
            }
        });

        // Render ingredient by default
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_content, IngredientFragment.class, null, "IngredientFragment")
                .setReorderingAllowed(true)
                .addToBackStack("IngredientFragment")
                .commit();
        bottomNav.getMenu().findItem(R.id.action_ingredients).setChecked(true);
    }

    /**
     * Renders the fragment with the given id
     * @param id id of menu item in the nav bar
     */
    private void renderFragment(int id) {
        // Avoid duplicates if you press button on same screen
        if (bottomNav.getSelectedItemId() == id) {return;}

        FragmentManager fragmentManager = getSupportFragmentManager();
        Class<? extends Fragment> f = null;
        String tag = null;

        if (id == R.id.action_ingredients) {
            f = IngredientFragment.class;
            tag = "IngredientFragment";
        } else if (id == R.id.action_recipes) {
            f = RecipeFragment.class;
            tag = "RecipeFragment";
        } else if (id == R.id.action_meal_plan) {
            f = MealPlanFragment.class;
            tag = "MealPlanFragment";
        } else if (id == R.id.action_shopping_list) {
            f = ShoppingListFragment.class;
            tag = "ShoppingListFragment";
        }

        fragmentManager.beginTransaction()
                .replace(R.id.nav_content, f, null, tag)
                .setReorderingAllowed(true)
                .addToBackStack(tag)
                .commit();

        bottomNav.getMenu().findItem(R.id.action_ingredients).setChecked(true);
    }

    /**
     * Override the back button so when we only have one fragment we close the app
     * instead of showing a blank screen
     */
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();

            // Set the correct button to be selected
            int index = getSupportFragmentManager().getBackStackEntryCount() - 1;
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(index);
            String tag = backEntry.getName();
            Integer id = null;

            if (Objects.equals(tag, "IngredientFragment")) {
                id = R.id.action_ingredients;
            } else if (Objects.equals(tag, "RecipeFragment")) {
                id = R.id.action_recipes;
            } else if (Objects.equals(tag, "MealPlanFragment")) {
                id = R.id.action_meal_plan;
            } else if (Objects.equals(tag, "ShoppingListFragment")) {
                id = R.id.action_shopping_list;
            }

            bottomNav.getMenu().findItem(id).setChecked(true);
        } else {
            finish();
        }
    }
}