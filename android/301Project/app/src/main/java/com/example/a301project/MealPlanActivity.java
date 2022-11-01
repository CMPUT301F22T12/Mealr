package com.example.a301project;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class MealPlanActivity extends NavActivity {
    private ListView listView;
    private ArrayAdapter<MealPlan> mealPlanArrayAdapter;
    private ArrayList<MealPlan> mealPlanDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // We have to put our layout in the space for the content
        ViewGroup content = findViewById(R.id.nav_content);
        getLayoutInflater().inflate(R.layout.activity_meal_plan, content, true);

        // Set the correct button to be selected
        bottomNav.getMenu().findItem(R.id.action_meal_plan).setChecked(true);

        // Get Data
        // TODO create a controller and hook up
        for (DayOfWeek d: DayOfWeek.values()) {
            mealPlanDataList.add(
                    new MealPlan(d.getDisplayName(TextStyle.FULL, Locale.CANADA))
            );
        }

        Log.i("day", String.valueOf(mealPlanDataList));

        // Attach to listview
        mealPlanArrayAdapter = new MealPlanListAdapter(this, mealPlanDataList);
        listView = findViewById(R.id.mealPlanListView);
        listView.setAdapter(mealPlanArrayAdapter);
    }
}