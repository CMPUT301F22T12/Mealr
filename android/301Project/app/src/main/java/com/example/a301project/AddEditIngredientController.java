package com.example.a301project;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddEditIngredientController {

    // connect to firebase and handle adding spinner items
    private String collectionName = "Customization";
    private String documentName = "IngredientCustomization";
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private DocumentReference documentReference;

    /**
     * The constructor for the {@link AddEditIngredientController}. Sets up the {@link #db} and {@link #collectionReference}
     * and {@link #documentReference}
     */
    public AddEditIngredientController() {
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection(collectionName);
        documentReference = collectionReference.document(documentName);
    }


    /**
     * A {@link AddEditIngredientController} getter for the {@link #documentReference
     * @return The {@link #documentReference}
     */
    public DocumentReference getDocumentReference() {return this.documentReference;}


    /**
     * Method to add an a category {@link String} to the Firebase database
     * @param category This is the {@link Ingredient} category {@link String} to add to the Firebase
     */
    /*
    public void addIngredientCategory(String category) {
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Map<String, Object> result = documentSnapshot.getData();
                ArrayList<CharSequence> categoryOptions = (ArrayList<CharSequence>) result.get("IngredientCategories");
                categoryOptions.add(category);
                result.replace("IngredientCategories", categoryOptions);

                // then rewrite the data
                documentReference.set(result, SetOptions.merge());
            }
        });
    }*/

    public void addIngredientCategory(String category) {
        addIngredientCustomization("IngredientCategories", category);
    }



    /**
     * Add an {@link Ingredient} location {@link String} to Firebase
     * @param location the location {@link String} to store
     */
    public void addIngredientLocation(String location) {
        addIngredientCustomization("IngredientLocations", location);
    }

    /**
     * Add an {@link Ingredient} unit {@link String} to Firebase
     * @param unit the unit {@link String} to store
     */
    public void addIngredientUnit(String unit) {
        addIngredientCustomization("IngredientUnits", unit);
    }


    /**
     * Adds an {@link Ingredient} spinner customization value to Firebase
     * @param listName {@link String} The name of the list field in the Firebase database
     * @param valueToAdd {@link String} The value to add to the list field in the Firebase database
     */
    private void addIngredientCustomization(String listName, String valueToAdd) {
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Map<String, Object> result = documentSnapshot.getData();
                ArrayList<CharSequence> customizationOptions = (ArrayList<CharSequence>) result.get(listName);
                customizationOptions.add(valueToAdd);
                result.replace(listName, customizationOptions);

                // then rewrite the data
                documentReference.set(result, SetOptions.merge());
            }
        });
    }
}
