package com.example.a301project;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This {@link IngredientController} class allows the {@link IngredientActivity} to communicate with
 * the Firestore database backend. This class contains methods to add or remove {@link Ingredient} objects to the
 * database, as well as edit functionality.
 *
 * This class should be used exclusively by the {@link IngredientActivity} class to handle database communication.
 */
public class IngredientController {
    // connect to firebase and handles add and delete
    private String collectionName = "Ingredient";
    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    /**
     * The constructor for the {@link IngredientController}. Sets up the {@link #db} and {@link #collectionReference}
     */
    public IngredientController() {
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection(collectionName);
    }

    /**
     * Converts a {@link String} to a {@link Timestamp} object
     * @param bestBefore The best before date as a {@link String}
     * @return A {@link Timestamp} object
     */
    public static Timestamp convertStringToTimestamp(String bestBefore) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(bestBefore);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Timestamp(date);
    }

    /**
     * A {@link IngredientController} getter for the {@link #collectionReference}
     * @return The {@link #collectionReference}
     */
    public CollectionReference getCollectionReference() {return this.collectionReference;}

    /**
     * Method to add an {@link Ingredient} to the Firebase database
     * @param ingredient This is the {@link Ingredient} to be added to Firebase
     */
    public void addIngredient(Ingredient ingredient) {
        // get all required values
        double amount = ingredient.getAmount();
        String bestBefore = ingredient.getbbd();
        String category = ingredient.getCategory();
        String location = ingredient.getLocation();
        String name = ingredient.getName();
        String unit = ingredient.getUnit();

        // convert string to Timestamp
        Timestamp bbd = convertStringToTimestamp(bestBefore);

        HashMap<String, Object> data = new HashMap<>();
        // put all the values into hashmap
        data.put("Amount", amount);
        data.put("BestBeforeDate", bbd);
        data.put("Category", category);
        data.put("Location", location);
        data.put("Name", name);
        data.put("Unit", unit);

        collectionReference
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    /**
                     * Method invoked when successfully added to database
                     * @param documentReference {@link DocumentReference} reference to document
                     */
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String id = documentReference.getId();
                        Log.d(TAG, "Added document with ID: " + id);
                        ingredient.setId(id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    /**
                     * Method invoked when failed to add to database
                     * @param e {@link Exception} the error that occured
                     */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document",e);
                    }
                });
    }

    /**
     * Method to remove an {@link Ingredient} from Firebase using its ID
     * @param ingredient This is the {@link Ingredient} to be removed from Firebase
     */
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

    /**
     * Notifies the Firestore database of an update to an ingredient. The database then updates the
     * ingredient's values with the provided {@link Ingredient} object
     * @param ingredient The {@link Ingredient} object to update in the database
     */
    public void notifyUpdate(Ingredient ingredient) {
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("Name",ingredient.getName());
        userMap.put("Amount",ingredient.getAmount());
        userMap.put("BestBeforeDate",convertStringToTimestamp(ingredient.getbbd()));
        userMap.put("Category",ingredient.getCategory());
        userMap.put("Unit",ingredient.getUnit());
        userMap.put("Location",ingredient.getLocation());
        db.collection(collectionName)
                .document(ingredient.getId())
                .update(userMap);
    }
}
