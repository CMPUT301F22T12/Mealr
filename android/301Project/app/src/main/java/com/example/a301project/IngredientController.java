package com.example.a301project;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class IngredientController {
    // connect to firebase and handles add and delete
    FirebaseFirestore db;
    public IngredientController() {
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Ingredients");
    }

}
