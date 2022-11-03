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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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

                startActivity(i);

                return true;
            }
        });
    }
}