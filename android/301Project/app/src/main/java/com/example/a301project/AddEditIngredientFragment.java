package com.example.a301project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * A class for a fragment that handles adding and editing ingredients
 * Fragment is activated when user clicks certain buttons
 */
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
    private Button deleteButton;
    private Ingredient currentIngredient;
    private boolean createNewIngredient;
    private ArrayList<CharSequence> unitOptions;
    private ArrayList<CharSequence> categoryOptions;
    private ArrayList<CharSequence> locationOptions;

    /**
     * Method that responds when the fragment has been interacted with
     * OnConfirmPressed either creates a new Ingredient or updates an existing one based on boolean createNewIngredient
     */
    public interface OnFragmentInteractionListener {
        void onConfirmPressed(Ingredient currentIngredient, boolean createNewIngredient);
    }



    /**
     * Method to set the fragment attributes
     * Sets the information of current ingredient if the tag is EDIT
     * Sets empty EditText views if the tag is ADD, and hides delete button
     * Creates new ingredient or resets information of current ingredient based on the tag
     * @param savedInstanceState {@link Bundle} the last saved instance state of fragment, NULL if
     *                                         fragment is newly created
     * @return dialog fragment with the appropriate fields
     */
    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_edit_ingredientlayout, null);

        // get the current date as the default in date picker
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        Bundle bundle = getArguments();
        if (bundle != null) {
            currentIngredient = (Ingredient) bundle.get("ingredient");
            createNewIngredient = (boolean) bundle.get("createNew");
        }

        // set variables of EditText fields
        ingredientName = view.findViewById(R.id.edit_name);
        bbdName = view.findViewById(R.id.edit_bbd);
        locationName = view.findViewById(R.id.edit_location);
        amountName = view.findViewById(R.id.edit_amount);
        unitName = view.findViewById(R.id.edit_unit);
        categoryName = view.findViewById(R.id.edit_category);
        deleteButton = view.findViewById(R.id.delete_ingredient_button);
        unitOptions = new ArrayList<>();
        categoryOptions = new ArrayList<>();
        locationOptions = new ArrayList<>();

        AddEditIngredientController addEditIngredientController = new AddEditIngredientController();
        DocumentReference documentReference = addEditIngredientController.getDocumentReference();


        // sets title of the fragment depending on whether the tag is ADD or EDIT
        String title;
        if (this.getTag().equals("ADD")) {
            title = "Add Entry";
            deleteButton.setVisibility(View.GONE);
        }
        else {
            title = "Edit Entry";
        }


        deleteButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Method for when Delete button is clicked
             * Another fragment pops up to confirm whether user meant to delete
             * @param view {@link View} the view of the fragment that was clicked
             */
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to delete this ingredient?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                IngredientController controller = new IngredientController();
                                controller.removeIngredient(currentIngredient);

                                Fragment frag = getParentFragmentManager().findFragmentByTag("EDIT");
                                getParentFragmentManager().beginTransaction().remove(frag).commit();
                                Toast.makeText(getContext(), "Ingredient Delete Successful", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            /**
                             * Method for when negative button is clicked in delete fragment
                             * @param dialog {@link DialogInterface} the interface of this pop up fragment
                             * @param id {@link Integer} ID of the recipe to be deleted
                             */
                            public void onClick(DialogInterface dialog, int id) {
                                // if No is pressed, return to Edit fragment
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        // Category spinner
        Resources res = getActivity().getResources();
        //List<CharSequence> categoryArray = List.of(res.getStringArray(R.array.category_array));
        //categoryOptions = new ArrayList<>(categoryArray);
        ArrayAdapter<CharSequence> categoryAdapter = new ArrayAdapter<>(this.getContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, categoryOptions);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryName.setAdapter(categoryAdapter);
        categoryName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Method invoked when a category in this view has been selected
             * @param adapterView {@link AdapterView} the AdapterView where the selection happened
             * @param view {@link View} the view that was clicked
             * @param i {@link Integer} position of the view in the adapter
             * @param l {@link Long} the row ID of the item that was selected
             */
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (categoryAdapter.getItem(i).equals("Add Category")) {
                    EditText customCategory = new EditText(getContext());
                    //customUnit.setVisibility(view.VISIBLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setView(customCategory);
                    builder.setMessage("Enter custom category")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // add the new category to the list and notify the adapter
                                    String newCategory = customCategory.getText().toString();
                                    int size = categoryOptions.size();
                                    categoryOptions.add(size-1, newCategory);
                                    categoryAdapter.notifyDataSetChanged();

                                    // add new category to firebase
                                    addEditIngredientController.addIngredientCategory(newCategory);

                                    // select the new category as the spinner value
                                    int j = categoryAdapter.getPosition(newCategory);
                                    categoryName.setSelection(j);
                                    currentIngredient.setCategory(newCategory);
                                }
                            }).show();
                }
                else {
                    // user didn't select the add custom option
                    currentIngredient.setCategory(adapterView.getItemAtPosition(i).toString());
                }
            }

            /**
             * Method invoked when nothing is selected
             * selection disappears from the view
             * @param adapterView {@link AdapterView} the AdapterView that contains no selected item
             */
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // nothing happens
            }
        });

        // Location spinner
        //List<CharSequence> locationsArray = List.of(res.getStringArray(R.array.location_array));
        //locationOptions = new ArrayList<>(locationsArray);
        ArrayAdapter<CharSequence> locationAdapter = new ArrayAdapter<>(this.getContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, locationOptions);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationName.setAdapter(locationAdapter);
        locationName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            /**
             * Method invoked when a location in this view has been selected
             * @param adapterView {@link AdapterView} the AdapterView where the selection happened
             * @param view {@link View} the view that was clicked
             * @param i {@link Integer} position of the view in the adapter
             * @param l {@link Long} the row ID of the item that was selected
             */
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (locationAdapter.getItem(i).equals("Add Location")) {
                    EditText customLocation = new EditText(getContext());
                    //customUnit.setVisibility(view.VISIBLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setView(customLocation);
                    builder.setMessage("Enter custom location")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String newLocation = customLocation.getText().toString();
                                    int size = locationOptions.size();
                                    locationOptions.add(size-1, newLocation);
                                    locationAdapter.notifyDataSetChanged();

                                    // add new location to firebase
                                    addEditIngredientController.addIngredientLocation(newLocation);

                                    // select the new location as the spinner value
                                    int j = locationAdapter.getPosition(newLocation);
                                    locationName.setSelection(j);
                                    currentIngredient.setLocation(newLocation);
                                }
                            }).show();
                }
                else {
                    // user didn't select the add custom option
                    //unitName.setSelection(i);
                    currentIngredient.setLocation(adapterView.getItemAtPosition(i).toString());
                }
            }

            /**
             * Method invoked when nothing is selected
             * selection disappears from the view
             * @param adapterView {@link AdapterView} the AdapterView that contains no selected item
             */
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // nothing happens
            }
        });


        // Unit spinner
        //List<CharSequence> unitsarray = List.of(res.getStringArray(R.array.units_array));
        //unitOptions = new ArrayList<>(unitsarray);
        ArrayAdapter<CharSequence> unitAdapter = new ArrayAdapter<>(this.getContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, unitOptions);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitName.setAdapter(unitAdapter);
        unitName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            /**
             * Method invoked when a unit in this view has been selected
             * @param adapterView {@link AdapterView} the AdapterView where the selection happened
             * @param view {@link View} the view that was clicked
             * @param i {@link Integer} position of the view in the adapter
             * @param l {@link Long} the row ID of the item that was selected
             */
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // if Add unit is selected, set the textbox to visible
                if (unitAdapter.getItem(i).equals("Add Unit")) {
                    EditText customUnit = new EditText(getContext());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setView(customUnit);
                    builder.setMessage("Enter custom unit")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String newUnit = customUnit.getText().toString();
                                    int size = unitOptions.size();
                                    unitOptions.add(size-1, newUnit);
                                    unitAdapter.notifyDataSetChanged();

                                    // add new unit to firebase
                                    addEditIngredientController.addIngredientUnit(newUnit);

                                    // select the new unit as the spinner value
                                    int j = unitAdapter.getPosition(newUnit);
                                    unitName.setSelection(j);
                                    currentIngredient.setUnit(newUnit);
                                }
                            }).show();
                }
                else {
                    // user didn't select the add custom option
                    currentIngredient.setUnit(adapterView.getItemAtPosition(i).toString());
                }
            }

            /**
             * Method invoked when nothing is selected
             * selection disappears from the view
             * @param adapterView {@link AdapterView} the AdapterView that contains no selected item
             */
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // nothing happens
            }
        });

        // gets the spinner value from firebase
        Task<DocumentSnapshot> documentSnapshot = documentReference.get();
        documentSnapshot.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> result = task.getResult().getData();

                    // category

                    categoryOptions.addAll((ArrayList<CharSequence>) result.get("IngredientCategories"));
                    categoryOptions.add("Add Category");
                    categoryAdapter.notifyDataSetChanged();

                    // location
                    locationOptions.addAll((ArrayList<CharSequence>) result.get("IngredientLocations"));
                    locationOptions.add("Add Location");
                    locationAdapter.notifyDataSetChanged();

                    // units
                    unitOptions.addAll(((ArrayList<CharSequence>) result.get("IngredientUnits")));
                    unitOptions.add("Add Unit");
                    unitAdapter.notifyDataSetChanged();

                    // set the spinners at the correct value for the current ingredient
                    locationName.setSelection(locationAdapter.getPosition(currentIngredient.getLocation()));
                    unitName.setSelection(unitAdapter.getPosition(currentIngredient.getUnit()));
                    categoryName.setSelection(categoryAdapter.getPosition(currentIngredient.getCategory()));
                }
            }
        });

        
        bbdName.setOnClickListener(new View.OnClickListener() {
            /**
             * Method invoked when the view is clicked
             * shows date picker
             * @param view {@link View} the view that contains the selected date
             */
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker = new DatePickerDialog(
                        getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
                datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePicker.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            /**
             * Method invoked a date is selected
             * sets the selected date as the best before date for this ingredient
             * @param datePicker {@link DatePicker} the date picker in view
             * @param year {@link Integer}  the year selected
             * @param month {@link Integer} the month selected
             * @param dayOfMonth {@link Integer} the day selected
             */
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
        //locationName.setSelection(locationAdapter.getPosition(currentIngredient.getLocation()));
        //unitName.setSelection(unitAdapter.getPosition(currentIngredient.getUnit()));
        amountName.setText(currentIngredient.getAmount().toString());
        //categoryName.setSelection(categoryAdapter.getPosition(currentIngredient.getCategory()));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    /**
                     * Method for getting and setting attributes of current ingredient
                     * @param dialogInterface {@link DialogInterface} the dialog interface of this fragment
                     * @param i {@link Integer} ID of the selected item
                     */
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // retrieve text from the text boxes
                        String ingredientName = AddEditIngredientFragment.this.ingredientName.getText().toString();
                        String bestbefore = AddEditIngredientFragment.this.bbdName.getText().toString();
                        String amount = AddEditIngredientFragment.this.amountName.getText().toString();
                        Double doubleAmount = 0.0;

                        // check if any field is empty
                        // if empty, reject add
                        boolean hasEmpty = ingredientName.isEmpty() || bestbefore.isEmpty() || amount.isEmpty();

                        if (hasEmpty) {
                            Toast.makeText(getContext(),  title + " Rejected: Missing Field(s)",Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            doubleAmount = Double.valueOf(amount);
                        }

                        // set the name of the current food as the edited fields
                        currentIngredient.setName(ingredientName);
                        currentIngredient.setBbd(bestbefore);
                        currentIngredient.setAmount(doubleAmount);

                        listener.onConfirmPressed(currentIngredient, createNewIngredient);
                    }
                }).create();
    }
    /**
     * Method to create a new AddEditRecipe fragment
     * @param ingredient {@link Ingredient} the current ingredient
     * @param createNew {@link boolean} variable that indicates whether to create a new ingredient
     * @return An Add/Edit Ingredient fragment
     */
    static AddEditIngredientFragment newInstance(Ingredient ingredient, boolean createNew, OnFragmentInteractionListener listener) {
        Bundle args = new Bundle();
        args.putSerializable("ingredient",ingredient);
        args.putSerializable("createNew", createNew);

        AddEditIngredientFragment fragment = new AddEditIngredientFragment();
        fragment.setArguments(args);

        fragment.listener = listener;
        return fragment;
    }
}

