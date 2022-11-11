package com.example.a301project;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Main Activity class for Ingredients
 * functionalities for add, edit, delete
 * initiates an IngredientController object that has access to firebase data
 * handles sorting of ingredients
 * void
 */

public class IngredientFragment extends Fragment implements AddEditIngredientFragment.OnFragmentInteractionListener {
    private IngredientController ingredientController;
    private ListView ingredientList;
    private ArrayAdapter<Ingredient> ingredientAdapter;
    private ArrayList<Ingredient> dataList;
    private String[] sortOptions = {"Name", "Location", "Expiry", "Category"};
    private Spinner sortSpinner;
    private Switch sortSwitch;
    public int position = -1;
    private Button addButton;

    public IngredientFragment() {
        super(R.layout.activity_ingredient);
    }

    /**
     * Method for initializing attributes of this activity
     * @param view The View returned by onCreateView.f
     * @param savedInstanceState {@link Bundle} the last saved instance of the fragment, NULL if newly created
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("My Ingredients");

        addButton = view.findViewById(R.id.add_ingredient_button);
        // create list of ingredients
        ingredientList = view.findViewById(R.id.ingredientListView);
        dataList = new ArrayList<>();

        ingredientAdapter = new CustomList(getContext(),dataList);
        ingredientList.setAdapter(ingredientAdapter);


        // ingredient sort by title, location, expiry date, category
        sortSpinner = view.findViewById(R.id.ingredientSortSpinner);
        sortSwitch = view.findViewById(R.id.ingredientSortSwitch);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(getContext(),com.google.android.material.R.layout.support_simple_spinner_dropdown_item, sortOptions);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Method invoked when a sort parameter in this view is selected
             * @param adapterView {@link AdapterView} the AdapterView where the selection happened
             * @param view {@link View} the view that was clicked
             * @param i {@link Integer} position of the view in the adapter
             * @param l {@link Long} the row ID of the item that was selected
             */
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // call the sortData function to determine which parameter to sort by (entry, location, category, unit)
                sortDataBySpinner();
            }

            /**
             * Method for when no spinner item is selected
             * @param adapterView {@link AdapterView} the AdapterView where the selection happened
             * @return void
             */
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // nothing happens
            }
        });
        sortSwitch.setChecked(true);
        sortSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /**
             * Method for when sort switch is clicked
             * @param compoundButton {@link CompoundButton} the switch button view that has changed
             * @param b {@link boolean} the checked state of the button
             * @return void
             */
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sortDataBySpinner();
            }
        });

        // on item  click in ingredient list, editor appears
        ingredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Method invoked when an ingredient in this view is clicked
             * @param adapterView {@link AdapterView} the AdapterView where the selection happened
             * @param view {@link View} the view that was clicked
             * @param i {@link Integer} position of the view in the adapter
             * @param l {@link Long} the row ID of the item that was selected
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // view ingredient details when you click on it
                position = i;
                Ingredient selected = (Ingredient) adapterView.getItemAtPosition(i);

                AddEditIngredientFragment.newInstance(ingredientAdapter.getItem(position),false, IngredientFragment.this).show(getChildFragmentManager(),"EDIT");

            }
        });

        // on add button click in ingredient list, adder appears
        addButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Method invoked when the add button is clicked
             * shows Add Entry fragment
             * @param view {@link View} the view that contains the add button
             */
            @Override
            public void onClick(View view) {
                Ingredient newIngredient = new Ingredient("",1,"","","","");
                AddEditIngredientFragment.newInstance(newIngredient,true, IngredientFragment.this).show(getChildFragmentManager(),"ADD");
            }
        });

        // ingredient controller to get data from firebase
        ingredientController = new IngredientController();
        CollectionReference collectionReference = ingredientController.getCollectionReference();
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            /**
             * Method for when data changes from collection reference
             * @param value {@link QuerySnapshot}
             * @param error {@link com.google.firebase.firestore.FirebaseFirestore}
             * @return void
             */
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                dataList.clear();
                // convert date picker date to a string
                for(QueryDocumentSnapshot doc: value) {
                    Date date = doc.getDate("BestBeforeDate");
                    String pattern = "yyyy-MM-dd";
                    DateFormat df = new SimpleDateFormat(pattern);
                    String bbd = df.format(date);

                    // create new ingredient in firebase
                    Ingredient newIngredient = new Ingredient(
                            doc.getString("Name"),
                            doc.getDouble("Amount"),
                            bbd,
                            doc.getString("Location"),
                            doc.getString("Unit"),
                            doc.getString("Category")
                    );
                    // set ID for new ingredient
                    newIngredient.setId(doc.getId());

                    dataList.add(newIngredient);
                }
                sortDataBySpinner();
                ingredientAdapter.notifyDataSetChanged();
            }
        });
    }
    /**
     * Method for sorting ingredients by selected attributes
     * sortable attributes: location, category, unit
     * sort switch will arrange in ascending or descending order
     * @return void
     */
    public void sortDataBySpinner() {
        if (getView() == null) return;
        sortSpinner = getView().findViewById(R.id.ingredientSortSpinner);
        sortSwitch = getView().findViewById(R.id.ingredientSortSwitch);

        // retrieve the sort information
        String sortBy = sortSpinner.getSelectedItem().toString();
        Integer asc = sortSwitch.isChecked() ? 1:-1;

        // sort ingredient list based on the sort option
        //
        Collections.sort(dataList, (Ingredient i1, Ingredient i2) -> {
                    if (sortBy.equals(sortOptions[0])) {
                        return asc * i1.getName().toLowerCase().compareTo(i2.getName().toLowerCase());
                    } else if (sortBy.equals(sortOptions[1])) {
                        return asc * i1.getLocation().toLowerCase().compareTo(i2.getLocation().toLowerCase());
                    } else if (sortBy.equals(sortOptions[2])) {
                        return asc * i1.getbbd().compareTo(i2.getbbd());
                    } else {
                        return asc * i1.getCategory().toLowerCase().compareTo(i2.getCategory().toLowerCase());
                    }
                }
        );
        ingredientAdapter.notifyDataSetChanged();
    }
    /**
     * Method for adding ingredients
     * trigger when Add button clicked
     * @param ingredient {@link Ingredient} an ingredient to be added
     * @return void
     */
    public void addIngredient(Ingredient ingredient) {
        ingredientAdapter.add(ingredient);
        ingredientController.addIngredient(ingredient);
    }

    /**
     * Method invoked when Add/Edit fragment confirm clicked
     * @param currentIngredient {@link Ingredient}
     * @param createNewIngredient {@link boolean}
     * checks whether to create new ingredient or to update existing
     * @return void
     */
    @Override
    public void onConfirmPressed(Ingredient currentIngredient, boolean createNewIngredient) {
        if (createNewIngredient) {
            addIngredient(currentIngredient);
        }
        else {
            ingredientController.notifyUpdate(currentIngredient);
        }
        ingredientAdapter.notifyDataSetChanged();
    }
}