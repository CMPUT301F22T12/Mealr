package com.example.a301project;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.Collections;

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

    private ArrayAdapter<ShoppingItem> shoppingListArrayAdapter;
    private ArrayList<ShoppingItem> shoppingItemDataList;
    private final ShoppingListController controller = new ShoppingListController();
    private ListView shoppingListView;
    private final String[] sortOptions = {"Name", "Category"};
    private Spinner sortSpinner;
    private Switch sortSwitch;
    private IngredientController ingredientController;
    private int selectedShoppingItem;
    private int listCount;
    private ConstraintLayout shoppingSort;
    private LottieAnimationView shoppingAnimation;


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

        // used when reading data from firebase
        listCount = 0;
        selectedShoppingItem = -1;
        ingredientController = new IngredientController();

        // get the layout item
        shoppingSort = view.findViewById(R.id.shoppingSort);
        shoppingAnimation = view.findViewById(R.id.shoppingAnimationView);

        // We have to put our layout in the space for the content
        ViewGroup content = view.findViewById(R.id.nav_content);
        getLayoutInflater().inflate(R.layout.activity_shopping_list, content, true);

        // Fetch the data -> get the Ingredients from both the Storage and the MealPlan -> used to calculate shopping list items
        controller.getIngredientStorageItems(this);
        controller.getMealPlanItems(this);

        // Attach to shoppingListView
        shoppingItemDataList = new ArrayList<>();
        shoppingListArrayAdapter = new ShoppingListAdapter(getContext(), shoppingItemDataList, ShoppingListFragment.this);
        shoppingListView= view.findViewById(R.id.shoppingItemListView);
        shoppingListView.setAdapter(shoppingListArrayAdapter);

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
        // notify the list adapter that the order of the data has changed
        shoppingListArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Called when the confirm button is pressed on the AddEditIngredientFragment
     * when a user is trying to add an Ingredient from the shopping list
     * @param currentIngredient The ingredient to add
     * @param createNewIngredient Whether to create a new ingredient or update a previous one
     */
    @Override
    public void onConfirmPressed(Ingredient currentIngredient, boolean createNewIngredient) {
        // add the ingredient and remove from shopping list
        ingredientController.addIngredient(currentIngredient);

        if (selectedShoppingItem >= 0) {
            // check if that amount of the ingredient purchased >= amount needed
            ShoppingItem shoppingItem =  shoppingListArrayAdapter.getItem(selectedShoppingItem);
            double amountNeeded = shoppingItem.getAmount();
            if (currentIngredient.getAmount() >= amountNeeded) {
                // purchased enough -> remove ingredient from shopping list
                shoppingItemDataList.remove(selectedShoppingItem);
                if (shoppingItemDataList.size() == 0) {
                    shoppingSort.setVisibility(View.GONE);
                    shoppingListView.setVisibility(View.GONE);
                    shoppingAnimation.setVisibility(View.VISIBLE);
                }
            } else {
                // update amount needed to purchase
                shoppingItem.setAmount(amountNeeded - currentIngredient.getAmount());
                shoppingItemDataList.set(selectedShoppingItem, shoppingItem);
            }
            shoppingListArrayAdapter.notifyDataSetChanged();
            selectedShoppingItem = -1;
        }
    }

    /**
     * Called when the "purchased" button is pressed on one of the shopping items in the listview
     * @param position The position of the {@link ShoppingItem} in the {@link ArrayAdapter} shoppingItemArrayAdapter
     */
    @Override
    public void onButtonPressed(int position) {
        // one of the shopping list items was checked
        selectedShoppingItem = position;
        ShoppingItem shoppingItem = shoppingListArrayAdapter.getItem(position);
        Ingredient selected = new Ingredient(
                shoppingItem.getName(),
                shoppingItem.getAmount(),
                shoppingItem.getbbd(),
                shoppingItem.getLocation(),
                shoppingItem.getUnit(),
                shoppingItem.getCategory()
        );

        // open the add/edit fragment but the "SHOPPING" tag
        AddEditIngredientFragment.newInstance(selected,false, ShoppingListFragment.this).show(getChildFragmentManager(),"SHOPPING");
    }

    /**
     * Called when the {@link ShoppingListController} is done reading a list from Firebase
     * After both Ingredients and MealPlan are done being read from Firebase, then
     * this method calls the {@link ShoppingListController} to calculate the items for the
     * shopping list
     * @param r The ArrayList of {@link ShoppingItem} that was read from Firebase
     */
    @Override
    public void f(ArrayList<ShoppingItem> r) {
        listCount++;
        // after both Ingredients and MealPlan have been read from Firebase
        if (listCount == 2) {
            controller.getShoppingItems(this);
        } else if (listCount == 3) {
            // this adds the calculated ShoppingItem ArrayList to the list to display on the screen
            shoppingItemDataList.addAll(r);
            shoppingListArrayAdapter.notifyDataSetChanged();
            if (r.size()>0) {
                shoppingSort.setVisibility(View.VISIBLE);
                shoppingListView.setVisibility(View.VISIBLE);
                shoppingAnimation.setVisibility(View.GONE);
            }
        }
    }
}