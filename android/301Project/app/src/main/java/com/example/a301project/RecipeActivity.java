package com.example.a301project;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecipeActivity extends NavActivity {
    private ListView listView;
    private ArrayAdapter<Recipe> recipeArrayAdapter;
    private ArrayList<Recipe> recipeDataList = new ArrayList<>();
    private RecipeController controller = new RecipeController(recipeDataList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // We have to put our layout in the space for the content
        ViewGroup content = findViewById(R.id.nav_content);
        getLayoutInflater().inflate(R.layout.activity_recipe, content, true);

        // Set the correct button to be selected
        bottomNav.getMenu().findItem(R.id.action_recipes).setChecked(true);

        // Fetch the data
        controller.getRecipes(res -> {
            recipeDataList.clear();
            recipeDataList.addAll(res);
            recipeArrayAdapter.notifyDataSetChanged();
        });

        // Attach to listView
        recipeArrayAdapter = new RecipeListAdapter(this, recipeDataList);
        listView = findViewById(R.id.recipeListView);
        listView.setAdapter(recipeArrayAdapter);

        // Style header
        ViewGroup header = content.findViewById(R.id.recipeHeader);
        for (int i = 0; i < header.getChildCount(); i++) {
            View child = header.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTypeface(null, Typeface.BOLD);
            }
        }
    }
}