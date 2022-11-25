package com.example.a301project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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
 * /**
 *  Main Activity class for Shopping List
 *  functionalities for add, edit, delete
 *  initiates an ShoppingListController object that has access to firebase data
 *  handles sorting
 *  @return void
 */
public class ShoppingListFragment extends Fragment implements ShoppingListAdapter.ShoppingListAdapterListener, AddEditIngredientFragment.OnFragmentInteractionListener,
ShoppingListController.ingredientItemSuccessHandler, ShoppingListController.shoppingItemSuccessHandler, ShoppingListController.mealPlanSuccessHandler {
    private ArrayAdapter<ShoppingItem> shoppingItemArrayAdapter;
    private ArrayList<ShoppingItem> shoppingItemDataList = new ArrayList<>();
    private final ArrayList<MealPlan> mealPlanItemDataList = new ArrayList<>();
    private final ShoppingListController controller = new ShoppingListController();
    private ListView shoppingListView;
    private final String[] sortOptions = {"Name", "Category"};
    private Spinner sortSpinner;
    private Switch sortSwitch;
    private int selectedShoppingItem;
    private IngredientController ingredientController;
    private int listCount = 0;

    public ShoppingListFragment() {
        super(R.layout.activity_shopping_list);
    }

    /**
     * Method for initializing attributes of this activity
     * @param view The View returned by onCreateView.f
     * @param savedInstanceState {@link Bundle} the last saved instance of the fragment, NULL if newly created
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("My Shopping List");

        selectedShoppingItem = -1;
        ingredientController = new IngredientController();

        // We have to put our layout in the space for the content
        ViewGroup content = view.findViewById(R.id.nav_content);
        getLayoutInflater().inflate(R.layout.activity_shopping_list, content, true);

        // Fetch the data
        //controller.getShoppingItems(res -> setShoppingItemDataList(res));
        //controller.getIngredientStorageItems(res -> setShoppingItemDataList(res)); // use this for NOW
        controller.getIngredientStorageItems(this);
        controller.getMealPlanItems(this);

        // Attach to shoppingListView
        shoppingItemArrayAdapter = new ShoppingListAdapter(getContext(), shoppingItemDataList, ShoppingListFragment.this);
        shoppingListView= view.findViewById(R.id.shoppingItemListView);
        shoppingListView.setAdapter(shoppingItemArrayAdapter);

        // Setup sorting
        sortSpinner = view.findViewById(R.id.shoppingSortSpinner);
        sortSwitch = view.findViewById(R.id.shoppingSortSwitch);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(getContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, sortOptions);
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
        sortDataBySpinner();
    }

    /**
     * Sets the internal shopping items list the new one.
     * @param a ArrayList of shopping items to set the data list to
     */
    private void setShoppingItemDataList(ArrayList<ShoppingItem> a) {
        shoppingItemDataList.clear();
        shoppingItemDataList.addAll(a);
        shoppingItemArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Sorts internal recipe list by selected parameters defined the sortOptions attribute
     * Sort by parameters: Title, Category
     */
    private void sortDataBySpinner() {
        if (getView() == null) return;
        // Make sure views are defined
        sortSpinner = getView().findViewById(R.id.shoppingSortSpinner);
        sortSwitch = getView().findViewById(R.id.shoppingSortSwitch);

        String sortBy = sortSpinner.getSelectedItem().toString();
        int asc = sortSwitch.isChecked() ? 1 : -1;

        // determine which sort option was selected, then sort them in ascending or descending order
        // ascending or descending is based on the asc variable
        Collections.sort(shoppingItemDataList, (ShoppingItem s1, ShoppingItem s2) -> {
                    if (sortBy.equals(sortOptions[0])) {
                        return asc * s1.getName().toLowerCase().compareTo(s2.getName().toLowerCase());
                    } else {
                        return asc * s1.getCategory().toLowerCase().compareTo(s2.getCategory().toLowerCase());
                    }
                }
        );

        shoppingItemArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConfirmPressed(Ingredient currentIngredient, boolean createNewIngredient) {
        // add the ingredient and remove from shopping list
        ingredientController.addIngredient(currentIngredient);

        if (selectedShoppingItem >= 0) {
            shoppingItemDataList.remove(selectedShoppingItem);
            shoppingItemArrayAdapter.notifyDataSetChanged();
            selectedShoppingItem = -1;
        }

    }

    @Override
    public void onButtonPressed(int position) {
        // one of the shopping list items was checked
        selectedShoppingItem = position;
        Ingredient selected = (Ingredient) shoppingItemArrayAdapter.getItem(position);

        // open the add/edit fragment but the "SHOPPING" tag
        AddEditIngredientFragment.newInstance(selected,false, ShoppingListFragment.this).show(getChildFragmentManager(),"SHOPPING");
    }

    @Override
    public void f(ArrayList<ShoppingItem> r) {
        listCount++;
        if (listCount == 1) {

        }
        if (listCount == 2) {
            controller.getShoppingItems(this);

        } else if (listCount == 3) {
            //shoppingItemDataList = r;
            shoppingItemDataList.addAll(r);
            shoppingItemArrayAdapter.notifyDataSetChanged();
        }
    }
}