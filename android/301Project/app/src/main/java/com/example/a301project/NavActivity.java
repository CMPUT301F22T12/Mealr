package com.example.a301project;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

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
                // retrive the selected item ID to determine which activity to switch to
                // in future prototypes this will become fragments
                int id = item.getItemId();
                Intent i = null;

                if (id == R.id.action_ingredients) {
                    i = new Intent(NavActivity.this, IngredientActivity.class);
                } else if (id == R.id.action_recipes) {
                    i = new Intent(NavActivity.this, RecipeActivity.class);
                } else if (id == R.id.action_meal_plan) {
                    i = new Intent(NavActivity.this, MealPlanActivity.class);
                } else if (id == R.id.action_shopping_list) {
                    i = new Intent(NavActivity.this, ShoppingListActivity.class);
                }

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                // launch the activity that was selected
                startActivity(i);

                return true;
            }
        });
    }
}