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

import java.util.ArrayList;
import java.util.Collections;

public class ShoppingListActivity extends NavActivity {
    private ListView listView;
    private ArrayAdapter<ShoppingItem> shoppingItemArrayAdapter;
    private ArrayList<ShoppingItem> shoppingItemDataList = new ArrayList<>();
    private ShoppingListController controller = new ShoppingListController();
    private final String[] sortOptions = {"Title", "Category"};
    private Spinner sortSpinner;
    private Switch sortSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We have to put our layout in the space for the content
        ViewGroup content = findViewById(R.id.nav_content);
        getLayoutInflater().inflate(R.layout.activity_shopping_list, content, true);

        // Set the correct button to be selected
        bottomNav.getMenu().findItem(R.id.action_shopping_list).setChecked(true);

        // Fetch the data
        controller.getShoppingItems(res -> setShoppingItemDataList(res));

        // Attach to listView
        shoppingItemArrayAdapter = new ShoppingListAdapter(this, shoppingItemDataList);
        listView = findViewById(R.id.shoppingItemListView);
        listView.setAdapter(shoppingItemArrayAdapter);

        // Setup sorting
        sortSpinner = findViewById(R.id.shoppingSortSpinner);
        sortSwitch = findViewById(R.id.shoppingSortSwitch);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, sortOptions);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortDataBySpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sortSwitch.setChecked(true);
        sortSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sortDataBySpinner();
            }
        });
        sortDataBySpinner();
    }

    /**
     * Sets the internal shopping items list the new one.
     *
     * @param a ArrayList of shopping items to set the data list to
     */
    private void setShoppingItemDataList(ArrayList<ShoppingItem> a) {
        shoppingItemDataList.clear();
        shoppingItemDataList.addAll(a);
        shoppingItemArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Sorts internal recipe list by selected filters defined the sortOptions attribute
     */
    private void sortDataBySpinner() {
        // Make sure views are defined
        sortSpinner = findViewById(R.id.shoppingSortSpinner);
        sortSwitch = findViewById(R.id.shoppingSortSwitch);

        String sortBy = sortSpinner.getSelectedItem().toString();
        Integer asc = sortSwitch.isChecked() ? 1 : -1;

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
}