package com.example.a301project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Class for an ArrayAdapter that renders MealPlan objects for use
 * by a ListView in MealPlanActivity
 */
public class MealPlanListAdapter extends ArrayAdapter<MealPlan> {
    private ArrayList<MealPlan> mealPlans;
    private Context context;

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

        // TODO: Render recipe and ingredient list items
        TextView name = view.findViewById(R.id.mp_nameText);

        name.setText(mp.getName());

        return view;
    }
}
