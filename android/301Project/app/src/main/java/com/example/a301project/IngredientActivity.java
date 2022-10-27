package com.example.a301project;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    Button addButton;
    Button removeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // We have to put our layout in the space for the content
        ViewGroup content = findViewById(R.id.nav_content);
        getLayoutInflater().inflate(R.layout.activity_ingredient, content, true);

        addButton = findViewById(R.id.add_ingredient_button);
        // create list of ingredients
        ingredientList = findViewById(R.id.ingredientListView);

//        //initialize attributes as empty
//        String []ingredients ={"pizza"};
//        String []locations = {"bed"};
//        String []bbds = {"2022-12-01"};
//        Integer []amounts = {1};
//        String []units = {"slice"};
//        String []categories = {"lunch"};

        dataList = new ArrayList<>();


//        //initialize dataList
//        for (int i=0; i<ingredients.length;i++) {
//            dataList.add(new Ingredient(ingredients[i],amounts[i],bbds[i],locations[i],units[i],categories[i]));
//            //nameUnit.put(ingredients[i],units[i]);
//        }


        ingredientAdapter = new CustomList(this,dataList);
        ingredientList.setAdapter(ingredientAdapter);

        ingredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // view ingredient details when you click on it
                position = i;
                Ingredient selected = (Ingredient) adapterView.getItemAtPosition(i);
                AddEditIngredientFragment.newInstance(ingredientAdapter.getItem(position),false).show(getSupportFragmentManager(),"EDIT");
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ingredient newIngredient = new Ingredient("",1,"","","","");
                AddEditIngredientFragment.newInstance(newIngredient,true).show(getSupportFragmentManager(),"ADD");
            }
        });

        ingredientController = new IngredientController();
        CollectionReference collectionReference = ingredientController.getCollectionReference();
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                dataList.clear();
                for(QueryDocumentSnapshot doc: value) {
                    String id = doc.getId();
                    String name = (String) doc.getData().get("Name");

                    Timestamp timestamp = (Timestamp) doc.getData().get("BestBeforeDate");
                    Date date = timestamp.toDate();
                    String pattern = "yyyy-MM-dd";
                    DateFormat df = new SimpleDateFormat(pattern);
                    String bbd = df.format(date);

                    String category = (String) doc.getData().get("Category");
                    String location = (String) doc.getData().get("Location");
                    String unit = (String) doc.getData().get("Unit");

                    long amountL = (long) doc.getData().get("Amount");
                    int amount = (int) amountL;

                    Ingredient newIngredient = new Ingredient(name,amount,bbd,location,unit,category);
                    newIngredient.setId(id);

                    dataList.add(newIngredient);
                }
                ingredientAdapter.notifyDataSetChanged();
            }
        });

        // Set the correct button to be selected
        bottomNav.getMenu().findItem(R.id.action_ingredients).setChecked(true);
    }

    public void addIngredient(Ingredient ingredient) {
        ingredientAdapter.add(ingredient);
        ingredientController.addIngredient(ingredient);
    }

    public void deleteIngredient(Ingredient ingredient) {
        ingredientAdapter.remove(ingredient);
        ingredientController.removeIngredient(ingredient);
    }

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