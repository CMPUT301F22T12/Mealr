package com.example.a301project;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
public class MealPlanFragment extends Fragment implements AddEditMealPlanFragment.OnFragmentInteractionListener{
    private ListView listView;
    private ArrayAdapter<MealPlan> mealPlanArrayAdapter;
    private ArrayList<MealPlan> mealPlanDataList = new ArrayList<>();
    private MealPlanController controller = new MealPlanController();
    Button addMealButton;
    public int position = -1;

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

        addMealButton = view.findViewById(R.id.add_meal_button);

        // Attach to listview
        mealPlanArrayAdapter = new MealPlanListAdapter(getContext(), mealPlanDataList);
        listView = view.findViewById(R.id.mealPlanListView);
        listView.setAdapter(mealPlanArrayAdapter);

        // Fetch the data
        controller.getMealPlan(meal -> setMealPlanDataList(meal));

        // We have to put our layout in the space for the content
        ViewGroup content = view.findViewById(R.id.nav_content);
        getLayoutInflater().inflate(R.layout.activity_meal_plan, content, true);


        // to add a meal plan
        addMealButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Method invoked when the add button is clicked
             * shows Add Recipe fragment
             * @param view {@link View} the view that contains the add button
             */
            @Override
            public void onClick(View view) {
                MealPlan newMealPlan = new MealPlan(new ArrayList<>(),new ArrayList<>(),"","", "");
                AddEditMealPlanFragment.newInstance(newMealPlan,true, MealPlanFragment.this).show(getChildFragmentManager(),"ADD");
            }
        });

        // by clicking on an item in list view, open edit menu
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
                // when a MealPlan item is clicked, open Edit Recipe fragment
                position = i;
                AddEditMealPlanFragment.newInstance(mealPlanArrayAdapter.getItem(position), false, MealPlanFragment.this).show(getChildFragmentManager(), "EDIT");
            }
        });
    }
    /**
     * Method to clear recipeDataList,
     * Resets the internal recipe list the new one.
     * @param m {@link ArrayList} list of recipes to set the data list to
     */
    private void setMealPlanDataList(ArrayList<MealPlan> m) {
        mealPlanDataList.clear();
        mealPlanDataList.addAll(m);
        mealPlanArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Method for adding recipes
     * trigger when Add button clicked
     * @param mealplan {@link MealPlan} a Meal plan to be added
     * @return void
     */
    public void addMealPlan(MealPlan mealplan) {
        controller.addMealPlan(mealplan);
        mealPlanArrayAdapter.add(mealplan);
        mealPlanArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConfirmPressed(MealPlan currentMealPlan, boolean createNewMeal) {
        if (createNewMeal) {
            addMealPlan(currentMealPlan);
        }
        else {
            controller.notifyUpdate(currentMealPlan);
        }
        mealPlanArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Method for deleting a recipe when confirm is clicked
     * @param currentMealPlan {@link MealPlan} the current meal plan to be deleted
     */
    @Override
    public void onDeleteConfirmed(MealPlan currentMealPlan) {
        mealPlanArrayAdapter.remove(currentMealPlan);
    }
}