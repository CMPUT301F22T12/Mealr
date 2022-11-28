package com.example.a301project;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("unchecked")
public class AddEditIngredientController {

    private final String collectionName = "Customization";
    private final String documentName = "IngredientCustomization";
    private final FirebaseFirestore db;
    private final CollectionReference collectionReference;
    private final DocumentReference documentReference;

    /**
     * The constructor for the {@link AddEditIngredientController}. Sets up the {@link #db} and {@link #collectionReference}
     * and {@link #documentReference}
     */
    public AddEditIngredientController() {
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user.getEmail() != null;

        // connect to firebase and handle adding spinner items
        collectionReference = db.collection("User").document(user.getEmail()).collection(collectionName);
        documentReference = collectionReference.document(documentName);
    }

    /**
     * Constructor for injecting a db for testing purposes
     * @param db the database
     */
    public AddEditIngredientController(FirebaseFirestore db) {
        this.db = db;
        collectionReference = db.collection(collectionName);
        documentReference = db.document(documentName);
    }

    /**
     * A {@link AddEditIngredientController} getter for the {@link #documentReference
     * @return The {@link #documentReference}
     */
    public DocumentReference getDocumentReference() {return this.documentReference;}

    /**
     * Add an {@link Ingredient} category {@link String} to Firebase
     * @param category the category {@link String} to add
     */
    public void addIngredientCategory(String category) {
        addIngredientCustomization("IngredientCategories", category);
    }

    /**
     * Add an {@link Ingredient} location {@link String} to Firebase
     * @param location the location {@link String} to add
     */
    public void addIngredientLocation(String location) {
       addIngredientCustomization("IngredientLocations", location);
    }

    /**
     * Add an {@link Ingredient} unit {@link String} to Firebase
     * @param unit the unit {@link String} to add
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

                // check if the document is empty and if so -> create a new hashmap
                if (result == null) {
                    result = new HashMap<>();
                }
                // check if the result hashmap contains the IngredientCustomization listName
                if (!result.containsKey(listName)) {
                    result.put(listName, new ArrayList<CharSequence>());
                }

                ArrayList<CharSequence> customizationOptions = (ArrayList<CharSequence>) result.get(listName);

                // check if the value to add already exists in firebase
                Iterator<CharSequence> listIterator = customizationOptions.iterator();
                Boolean exists = false;
                while (listIterator.hasNext()) {
                    if (listIterator.next().toString().equalsIgnoreCase(valueToAdd)) {
                        exists = true;
                        break;
                    }
                }

                // if the value isn't already in the list -> then add it
                if (!exists) {
                    customizationOptions.add(valueToAdd);
                    result.replace(listName, customizationOptions);

                    // then rewrite the data
                    documentReference.set(result, SetOptions.merge());
                }
            }

        });
    }
}
