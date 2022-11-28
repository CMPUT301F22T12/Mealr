package com.example.a301project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for an ArrayAdapter that renders MealPlan objects for use
 * by a ListView in MealPlanFragment
 */
public class MealPlanListAdapter extends ArrayAdapter<MealPlan> {
    private final ArrayList<MealPlan> mealPlans;
    private final Context context;
    private HashMap<String, ArrayList<String>> ingredientHashMap;

    /**
     * Constructor for the adapter, takes context and ArrayList of MealPlan
     * @param context {@link Context} context of the layout to render
     * @param mealPlans {@link ArrayList} array of MealPlan to render
     */
    public MealPlanListAdapter(@NonNull Context context, ArrayList<MealPlan> mealPlans) {
        super(context, 0, mealPlans);
        this.mealPlans = mealPlans;
        this.context = context;
    }

    /**
     * Method for creating a view that will appear in the MealPlan adapter
     * @param position {@link Integer} the position of the current view
     * @param convertView {@link View} the reused view to be retrieved
     * @param parent {@link ViewGroup} the collection of views that contains current view
     * @return a view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MealPlan mp = mealPlans.get(position);

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.meal_plan_layout, parent, false);
        }

        // Find text views and list views for meal plan
        TextView name = view.findViewById(R.id.mp_nameText);
        TextView startDate = view.findViewById(R.id.startDateTextView);
        TextView endDate = view.findViewById(R.id.endDateTextView);
        ExpandableListView ingredientListView = view.findViewById(R.id.mp_ingredientList);
        ExpandableListView recipeListView = view.findViewById(R.id.mp_recipeList);

        // get the recipes of the meal plan selected
        ArrayList<Recipe> recipeDataList = mp.getRecipes();
            // filter recipe by name
        // get the ingredients of the meal plan selected
        ArrayList<Ingredient> ingredientDataList = mp.getIngredients();

        // set the text to each field
        name.setText(mp.getName());
        startDate.setText(mp.getStartDate());
        endDate.setText(mp.getEndDate());

        // attach recipe list adapter to recipe list view
//        ArrayAdapter<Recipe> recipeArrayAdapter = new RecipeListAdapter(getContext(), recipeDataList);
        //recipeListView.setAdapter(recipeArrayAdapter);
        // attach ingredient list adapter to ingredient list view
        ingredientHashMap = IngredientExpandablePump.getData(ingredientDataList);
        ExpandableListAdapter ingredientArrayAdapter =  new IngredientExpandableListAdapter(getContext(),ingredientDataList, ingredientHashMap);
        ingredientListView.setAdapter(ingredientArrayAdapter);


        return view;
    }
}
