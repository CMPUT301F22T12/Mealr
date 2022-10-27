package com.example.a301project;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddEditIngredientFragment extends DialogFragment {
    // fragment used for adding and editing an ingredient
    private EditText ingredientName;
    private EditText amountName;
    private EditText locationName;
    private EditText unitName;
    private EditText bbdName;
    private EditText categoryName;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onConfirmPressed(Ingredient currentIngredient, boolean createNewIngredient);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddEditIngredientFragment.OnFragmentInteractionListener) {
            listener = (AddEditIngredientFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement OnFragmentInteractionListener");
        }
    }

    Ingredient currentIngredient;
    boolean createNewIngredient;

    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_edit_ingredientlayout, null);

        Bundle bundle = getArguments();
        if (bundle != null) {
            currentIngredient = (Ingredient) bundle.get("ingredient");
            createNewIngredient = (boolean) bundle.get("createNew");
        }

        // set variables
        ingredientName = view.findViewById(R.id.edit_name);
        bbdName = view.findViewById(R.id.edit_bbd);
        locationName = view.findViewById(R.id.edit_location);
        amountName = view.findViewById(R.id.edit_amount);
        unitName = view.findViewById(R.id.edit_unit);
        categoryName = view.findViewById(R.id.edit_category);

        // set EditText boxes to the specific fields of the current selected Food
        ingredientName.setText(currentIngredient.getName());
        bbdName.setText(currentIngredient.getbbd());
        locationName.setText(currentIngredient.getLocation());
        unitName.setText(currentIngredient.getUnit());
        amountName.setText(currentIngredient.getAmount().toString());
        categoryName.setText(currentIngredient.getCategory());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add/Edit Entry")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // retrieve text from the text boxes
                        String ingredientName = AddEditIngredientFragment.this.ingredientName.getText().toString();
                        String bestbefore = AddEditIngredientFragment.this.bbdName.getText().toString();
                        String location = AddEditIngredientFragment.this.locationName.getText().toString();
                        String amount = AddEditIngredientFragment.this.amountName.getText().toString();
                        String unit = AddEditIngredientFragment.this.unitName.getText().toString();
                        String category = AddEditIngredientFragment.this.categoryName.getText().toString();
                        Log.d(TAG, category);
                        Integer intAmount = Integer.valueOf(amount);

                        // set the name of the current food as the edited fields
                        currentIngredient.setName(ingredientName);
                        currentIngredient.setBbd(bestbefore);
                        currentIngredient.setLocation(location);
                        currentIngredient.setAmount(intAmount);
                        currentIngredient.setUnit(unit);
                        currentIngredient.setCategory(category);


                        listener.onConfirmPressed(currentIngredient, createNewIngredient);
                    }
                }).create();

    }
    static AddEditIngredientFragment newInstance(Ingredient ingredient, boolean createNew) {
        Bundle args = new Bundle();
        args.putSerializable("ingredient",ingredient);
        args.putSerializable("createNew", createNew);

        AddEditIngredientFragment fragment = new AddEditIngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

