package com.example.a301project;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Class for MealPlan activity that contains a listview
 * an ArrayAdapter for the meal plan
 * renders MealPlan for user and allows them to add, delete, modify it
 * currently incomplete
 */
public class MealPlanFragment extends Fragment {
    private final ArrayList<MealPlan> mealPlanDataList = new ArrayList<>();

    public MealPlanFragment() {
        super(R.layout.activity_meal_plan);
    }

    /**
     * Method for initializing attributes of this activity
     * @param savedInstanceState {@link Bundle} the last saved instance of the fragment, NULL if newly created
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("My Meal Plans");

        // We have to put our layout in the space for the content
        ViewGroup content = view.findViewById(R.id.nav_content);
        getLayoutInflater().inflate(R.layout.activity_meal_plan, content, true);

        // Get Data
        // TODO create a controller and hook up to Firebase
        for (DayOfWeek d: DayOfWeek.values()) {
            mealPlanDataList.add(
                    new MealPlan(d.getDisplayName(TextStyle.FULL, Locale.CANADA))
            );
        }

        // Attach to listview
        ArrayAdapter<MealPlan> mealPlanArrayAdapter = new MealPlanListAdapter(getContext(), mealPlanDataList);
        ListView listView = view.findViewById(R.id.mealPlanListView);
        listView.setAdapter(mealPlanArrayAdapter);
    }
}