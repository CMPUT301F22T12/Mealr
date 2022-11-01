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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.lang.reflect.Array;
import java.util.Calendar;

public class AddEditIngredientFragment extends DialogFragment {
    // fragment used for adding and editing an ingredient
    private EditText ingredientName;
    private EditText amountName;
    private EditText locationName;
    private Spinner unitName;
    private EditText bbdName;
    private EditText categoryName;
    private OnFragmentInteractionListener listener;
    private DatePickerDialog.OnDateSetListener setListener;
    private Button deleteButton;

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
        deleteButton = view.findViewById(R.id.delete_ingredient_button);

        String title;
        if (this.getTag().equals("ADD")) {
            title = "Add Entry";
            deleteButton.setVisibility(View.GONE);
        }
        else {
            title = "Edit Entry";
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to delete this ingredient?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                IngredientController controller = new IngredientController();
                                controller.removeIngredient(currentIngredient);

                                Fragment frag = getActivity().getSupportFragmentManager().findFragmentByTag("EDIT");
                                getActivity().getSupportFragmentManager().beginTransaction().remove(frag).commit();
                                Toast.makeText(getContext(), "Ingredient Delete Successful", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.units_array, R.layout.ingredient_unit_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitName.setAdapter(spinnerAdapter);
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
        locationName.setText(currentIngredient.getLocation());
        unitName.setSelection(spinnerAdapter.getPosition(currentIngredient.getUnit()));
        amountName.setText(currentIngredient.getAmount().toString());
        categoryName.setText(currentIngredient.getCategory());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // retrieve text from the text boxes
                        String ingredientName = AddEditIngredientFragment.this.ingredientName.getText().toString();
                        String bestbefore = AddEditIngredientFragment.this.bbdName.getText().toString();
                        String location = AddEditIngredientFragment.this.locationName.getText().toString();
                        String amount = AddEditIngredientFragment.this.amountName.getText().toString();
                        String category = AddEditIngredientFragment.this.categoryName.getText().toString();
                        Log.d(TAG, category);
                        Double doubleAmount = Double.valueOf(amount);

                        boolean hasEmpty = ingredientName.isEmpty() || bestbefore.isEmpty() ||
                                           location.isEmpty() || amount.isEmpty() || category.isEmpty();

                        if (hasEmpty) {
                            Toast.makeText(getContext(),  title + " Rejected: Missing Field(s)",Toast.LENGTH_LONG).show();
                            return;
                        }

                        // set the name of the current food as the edited fields
                        currentIngredient.setName(ingredientName);
                        currentIngredient.setBbd(bestbefore);
                        currentIngredient.setLocation(location);
                        currentIngredient.setAmount(doubleAmount);
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

