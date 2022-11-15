package com.example.a301project;

import androidx.fragment.app.DialogFragment;

public class AddEditMealPlanFragment extends DialogFragment {
    private AddEditMealPlanFragment.OnFragmentInteractionListener listener;
    public interface OnFragmentInteractionListener {
        void onConfirmPressed(MealPlan currentMealPlan, boolean createNewMeal);
        void onDeleteConfirmed(MealPlan currentMealPlan);

    }
}
