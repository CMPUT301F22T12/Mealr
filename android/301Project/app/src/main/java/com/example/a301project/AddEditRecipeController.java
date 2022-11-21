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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user.getEmail() != null;
        collectionReference = db.collection("User").document(user.getEmail()).collection(collectionName);
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
                String listName = "RecipeCategories";

                // check if the document is empty and if so -> create a new hashmap
                if (result == null) {
                    result = new HashMap<>();
                }
                // check if the result hashmap contains the RecipeCategories array
                if (!result.containsKey(listName)) {
                    result.put(listName, new ArrayList<CharSequence>());
                }

                ArrayList<CharSequence> customizationOptions = (ArrayList<CharSequence>) result.get(listName);

                // check if the value to add already exists in firebase
                Iterator<CharSequence> listIterator = customizationOptions.iterator();
                Boolean exists = false;
                while (listIterator.hasNext()) {
                    if (listIterator.next().toString().equalsIgnoreCase(newCategory)) {
                        exists = true;
                        break;
                    }
                }

                // if the value isn't already in the list -> then add it
                if (!exists) {
                    customizationOptions.add(newCategory);
                    result.replace(listName, customizationOptions);

                    // then rewrite the data
                    documentReference.set(result, SetOptions.merge());
                }
            }
        });
    }
}
