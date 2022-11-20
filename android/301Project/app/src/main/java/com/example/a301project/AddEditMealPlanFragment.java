package com.example.a301project;

import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;

public class AddEditMealPlanFragment extends DialogFragment {
    private ListView ingredientList;
    private Button addIngredientButton;
    private AddEditMealPlanFragment.OnFragmentInteractionListener listener;
    public interface OnFragmentInteractionListener {
        void onConfirmPressed(MealPlan currentMealPlan, boolean createNewMeal);
        void onDeleteConfirmed(MealPlan currentMealPlan);

    }
}
