package com.example.a301project;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class IngredientActivity extends NavActivity {
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
            dataList.add(new Ingredient(ingredients[i],locations[i],bbds[i],amounts[i],units[i],categories[i]));
        }
        ingredientAdapter = new CustomList(this,dataList);
        ingredientList.setAdapter(ingredientAdapter);



        // Set the correct button to be selected
        bottomNav.getMenu().findItem(R.id.action_ingredients).setChecked(true);
    }
}