package com.example.a301project;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.lang.reflect.Array;
import java.util.Calendar;

public class AddEditIngredientFragment extends DialogFragment {
    // fragment used for adding and editing an ingredient
    private EditText ingredientName;
    private EditText amountName;
    private Spinner locationName;
    private Spinner unitName;
    private EditText bbdName;
    private Spinner categoryName;
    private OnFragmentInteractionListener listener;
    private DatePickerDialog.OnDateSetListener setListener;

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

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

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

        // Category spinner
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.category_array, R.layout.ingredient_unit_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryName.setAdapter(categoryAdapter);
        categoryName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryName.setSelection(i);
                currentIngredient.setCategory(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Location spinner
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.location_array, R.layout.ingredient_unit_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationName.setAdapter(locationAdapter);
        locationName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                locationName.setSelection(i);
                currentIngredient.setLocation(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // Unit spinner
        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.units_array, R.layout.ingredient_unit_item);

        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitName.setAdapter(unitAdapter);
        unitName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                unitName.setSelection(i);
                currentIngredient.setUnit(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bbdName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker = new DatePickerDialog(
                        getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener, year, month, day);
                datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePicker.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", dayOfMonth);
                bbdName.setText(date);
            }
        };

        // set EditText boxes to the specific fields of the current selected Food
        ingredientName.setText(currentIngredient.getName());
        bbdName.setText(currentIngredient.getbbd());
        locationName.setSelection(locationAdapter.getPosition(currentIngredient.getLocation()));
        unitName.setSelection(unitAdapter.getPosition(currentIngredient.getUnit()));
        amountName.setText(currentIngredient.getAmount().toString());
        categoryName.setSelection(categoryAdapter.getPosition(currentIngredient.getCategory()));
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
                        String amount = AddEditIngredientFragment.this.amountName.getText().toString();
                        Double doubleAmount = Double.valueOf(amount);

                        // check if any field is empty
                        boolean hasEmpty = ingredientName.isEmpty() || bestbefore.isEmpty() || amount.isEmpty();

                        if (hasEmpty) {
                            Toast.makeText(getContext(), "Add/Edit Rejected: Missing Field(s)",Toast.LENGTH_LONG).show();
                            return;
                        }

                        // set the name of the current food as the edited fields
                        currentIngredient.setName(ingredientName);
                        currentIngredient.setBbd(bestbefore);
                        currentIngredient.setAmount(doubleAmount);

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

