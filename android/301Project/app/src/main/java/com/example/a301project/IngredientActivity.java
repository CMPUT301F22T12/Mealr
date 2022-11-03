package com.example.a301project;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
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
 * @return void
 */

public class IngredientActivity extends NavActivity implements AddEditIngredientFragment.OnFragmentInteractionListener {
    private IngredientController ingredientController;
    private ListView ingredientList;
    private ArrayAdapter<Ingredient> ingredientAdapter;
    private Spinner ingredientSpinner;
    private ArrayList<Ingredient> dataList;
    private String[] sortOptions = {"Entry", "Location", "Expiry", "Category"};
    private Spinner sortSpinner;
    private Switch sortSwitch;
    public int position = -1;

    private Button addButton;
    private  Button removeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // We have to put our layout in the space for the content
        ViewGroup content = findViewById(R.id.nav_content);
        getLayoutInflater().inflate(R.layout.activity_ingredient, content, true);

        addButton = findViewById(R.id.add_ingredient_button);
        // create list of ingredients
        ingredientList = findViewById(R.id.ingredientListView);
        dataList = new ArrayList<>();

        ingredientAdapter = new CustomList(this,dataList);
        ingredientList.setAdapter(ingredientAdapter);


        // ingredient sort by title, location, expiry date, category
        sortSpinner = findViewById(R.id.ingredientSortSpinner);
        sortSwitch = findViewById(R.id.ingredientSortSwitch);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(this,com.google.android.material.R.layout.support_simple_spinner_dropdown_item, sortOptions);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                /**
                 * Method for when spinner item is selected
                 * sort spinner
                 * @return void
                 */
                sortDataBySpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                /**
                 * Method for when no spinner item is selected
                 * @return void
                 */
                // nothing happens
            }
        });
        sortSwitch.setChecked(true);
        sortSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                /**
                 * Method for when no spinner item is selected
                 * sort spinner
                 * @return void
                 */
                sortDataBySpinner();
            }
        });
        sortDataBySpinner();

        // on item  click in ingredient list, editor appears
        ingredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /**
                 * Method for an ingredient is clicked in list view
                 * open Edit menu
                 * @return void
                 */
                // view ingredient details when you click on it
                position = i;
                Ingredient selected = (Ingredient) adapterView.getItemAtPosition(i);
                AddEditIngredientFragment.newInstance(ingredientAdapter.getItem(position),false).show(getSupportFragmentManager(),"EDIT");

            }
        });

        // on add button click in ingredient list, adder appears
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ingredient newIngredient = new Ingredient("",1,"","","","");
                AddEditIngredientFragment.newInstance(newIngredient,true).show(getSupportFragmentManager(),"ADD");
            }
        });

        // ingredient controller to get data from firebase
        ingredientController = new IngredientController();
        CollectionReference collectionReference = ingredientController.getCollectionReference();
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            /**
             * Method for retrieving data information from firebase
             * convert Best before date string to timestamp
             * @return void
             */
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                dataList.clear();
                for(QueryDocumentSnapshot doc: value) {
                    Date date = doc.getDate("BestBeforeDate");
                    String pattern = "yyyy-MM-dd";
                    DateFormat df = new SimpleDateFormat(pattern);
                    String bbd = df.format(date);

                    Ingredient newIngredient = new Ingredient(
                            doc.getString("Name"),
                            doc.getDouble("Amount"),
                            bbd,
                            doc.getString("Location"),
                            doc.getString("Unit"),
                            doc.getString("Category")
                    );
                    newIngredient.setId(doc.getId());

                    dataList.add(newIngredient);
                }
                ingredientAdapter.notifyDataSetChanged();
            }
        });

        // Set the correct button to be selected
        bottomNav.getMenu().findItem(R.id.action_ingredients).setChecked(true);
    }

    public void sortDataBySpinner() {
        /**
         * Method for sorting ingredients by selected attributes
         * sort switch will arrange in ascending or descending order
         * @return void
         */
        sortSpinner = findViewById(R.id.ingredientSortSpinner);
        sortSwitch = findViewById(R.id.ingredientSortSwitch);

        // retrieve the sort information
        String sortBy = sortSpinner.getSelectedItem().toString();
        Integer asc = sortSwitch.isChecked() ? 1:-1;

        Collections.sort(dataList, (Ingredient i1, Ingredient i2) -> {
                    if (sortBy.equals(sortOptions[0])) {
                        return asc * i1.getName().compareTo(i2.getName());
                    } else if (sortBy.equals(sortOptions[1])) {
                        return asc * i1.getLocation().compareTo(i2.getLocation());
                    } else if (sortBy.equals(sortOptions[2])) {
                        return asc * i1.getbbd().compareTo(i2.getbbd());
                    } else {
                        return asc * i1.getCategory().compareTo(i2.getCategory());
                    }
                }
        );
        ingredientAdapter.notifyDataSetChanged();
    }

    public void addIngredient(Ingredient ingredient) {
        /**
         * Method for adding ingredients
         * trigger when Add button clicked
         * @return void
         */
        ingredientAdapter.add(ingredient);
        ingredientController.addIngredient(ingredient);
    }

    public void deleteIngredient(Ingredient ingredient) {
        /**
         * Method for deleting ingredient
         * attempting to implement recycleview
         * @return void
         */
        ingredientAdapter.remove(ingredient);
        ingredientController.removeIngredient(ingredient);
    }

    @Override
    public void onConfirmPressed(Ingredient currentIngredient, boolean createNewIngredient) {
        /**
         * Method for Add/Edit fragment confirm
         * checks whether to create new ingredient or to update existing
         * @return void
         */
        if (createNewIngredient) {
            addIngredient(currentIngredient);
        }
        else {
            ingredientController.notifyUpdate(currentIngredient);
        }
        ingredientAdapter.notifyDataSetChanged();
    }
}