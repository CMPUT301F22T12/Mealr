package com.example.a301project;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class IngredientController {
    // connect to firebase and handles add and delete
    String collectionName = "Ingredient";
    FirebaseFirestore db;
    final CollectionReference collectionReference = db.collection(collectionName);

    public IngredientController() {
        db = FirebaseFirestore.getInstance();
    }

    public void addIngredient(Ingredient ingredient) {
        // get all required values
        int amount = ingredient.getAmount();
        String bestBefore = ingredient.getbbd();
        String category = ingredient.getCategory();
        String location = ingredient.getLocation();
        String name = ingredient.getName();
        String unit = ingredient.getUnit();

        HashMap<String, Object> data = new HashMap<>();

        // put all the values into hashmap
        data.put("Amount", amount);
        data.put("BestBeforeDate", bestBefore);
        data.put("Category", category);
        data.put("Location", location);
        data.put("Name", name);
        data.put("Unit", unit);

        collectionReference
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String id = documentReference.getId();
                        Log.d(TAG, "Added document with ID: " + id);
                        ingredient.setId(id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document",e);
                    }
                });
    }

    public void removeIngredient(Ingredient ingredient) {
        String id = ingredient.getId();
        db.collection(collectionName).document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Successfully deleted document with ID: " + id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Could not delete document with ID: " + id,e);
                    }
                });
    }
}
