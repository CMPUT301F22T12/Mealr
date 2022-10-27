package com.example.a301project;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class IngredientActivity extends NavActivity implements AddEditIngredientFragment.OnFragmentInteractionListener {
    private IngredientController ingredientController;
    private ListView ingredientList;
    private ArrayAdapter<Ingredient> ingredientAdapter;
    private Spinner ingredientSpinner;
    private ArrayList<Ingredient> dataList;
    public int position = -1;
    final String[] sortBy = {"Entry","Best Before","Location","Category" };
    private Switch sortSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // We have to put our layout in the space for the content
        ViewGroup content = findViewById(R.id.nav_content);
        getLayoutInflater().inflate(R.layout.activity_ingredient, content, true);

        // create list of ingredients
        ingredientList = findViewById(R.id.ingredientListView);

        //initialize attributes as empty
        String []ingredients ={"pizza"};
        String []locations = {"bed"};
        String []bbds = {"2022-12-01"};
        Integer []amounts = {1};
        String []units = {"slice"};
        String []categories = {"lunch"};

        dataList = new ArrayList<>();

        //initialize dataList
        for (int i=0; i<ingredients.length;i++) {
            dataList.add(new Ingredient(ingredients[i],amounts[i],bbds[i],locations[i],units[i],categories[i]));
        }

        ingredientAdapter = new CustomList(this,dataList);
        ingredientList.setAdapter(ingredientAdapter);

        ingredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // view ingredient details when you click on it
                position = i;
                Ingredient selected = (Ingredient) adapterView.getItemAtPosition(i);
                AddEditIngredientFragment.newInstance(ingredientAdapter.getItem(position)).show(getSupportFragmentManager(),"EDIT");

            }
        });

        // Set the correct button to be selected
        bottomNav.getMenu().findItem(R.id.action_ingredients).setChecked(true);
    }

    @Override
    public void onConfirmPressed(Ingredient currentIngredient) {
        ingredientAdapter.notifyDataSetChanged();
    }
}