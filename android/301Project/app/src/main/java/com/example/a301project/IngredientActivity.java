package com.example.a301project;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class IngredientActivity extends NavActivity implements AddEditIngredientFragment.OnFragmentInteractionListener {
    IngredientController ingredientController;
    ListView ingredientList;
    ArrayAdapter<Ingredient> ingredientAdapter;
    ArrayList<Ingredient> dataList;
    public int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // We have to put our layout in the space for the content
        ViewGroup content = findViewById(R.id.nav_content);
        getLayoutInflater().inflate(R.layout.activity_ingredient, content, true);
        FirebaseFirestore db;

        // create list of ingredients
        ingredientList = findViewById(R.id.IngredientList);
        // store units as a sub item in a listview
        HashMap<String, String> nameUnit = new HashMap<>();

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
            nameUnit.put(ingredients[i],units[i]);
        }
        // hashmap to map ingredient name and unit as a pair, so that unit can be the subitems in listview
        List<HashMap<String,String>> sublist = new ArrayList<>();
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, sublist, R.layout.ingredient_content,
                new String[]{"Ingredient","Unit"},
                new int[]{R.id.ingredient_text, R.id.unit_text});

        Iterator it = nameUnit.entrySet().iterator();
        while (it.hasNext()) {
            HashMap<String, String> resultMap = new HashMap<>();
            Map.Entry pair = (Map.Entry)it.next();
            resultMap.put("Ingredient",pair.getKey().toString());
            resultMap.put("Unit",pair.getValue().toString());
            sublist.add(resultMap);
        }
        ingredientList.setAdapter(simpleAdapter);

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