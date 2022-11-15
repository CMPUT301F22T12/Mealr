package com.example.a301project;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Map;

public class AddEditRecipeController {

    // connect to firebase and handle adding spinner items
    private String collectionName = "Customization";
    private String documentName = "RecipeCustomization";
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private DocumentReference documentReference;

    /**
     * The constructor for the {@link AddEditRecipeController}. Sets up the {@link #db} and {@link #collectionReference}
     * and {@link #documentReference}
     */
    public AddEditRecipeController() {
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection(collectionName);
        documentReference = collectionReference.document(documentName);
    }

    /**
     * A {@link AddEditRecipeController} getter for the {@link #documentReference
     * @return The {@link #documentReference}
     */
    public DocumentReference getDocumentReference() {return this.documentReference;}

    /**
     * Adds an {@link Recipe} spinner category {@link String} value to Firebase
     * @param newCategory {@link String} The new category value {@link String} to add to Firebase
     */
    public void addRecipeCategory(String newCategory) {
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Map<String, Object> result = documentSnapshot.getData();
                ArrayList<CharSequence> customizationOptions = (ArrayList<CharSequence>) result.get("RecipeCategories");
                customizationOptions.add(newCategory);
                result.replace("RecipeCategories", customizationOptions);

                // then rewrite the data
                documentReference.set(result, SetOptions.merge());
            }
        });
    }
}
