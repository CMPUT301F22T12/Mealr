package com.example.a301project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MealPlanController {
    private FirebaseFirestore db;
    private CollectionReference cr;
    private final String collectionName = "MealPlan";

    /**
     * The constructor for the {@link MealPlanController}. Sets up the {@link #db} and {@link #cr}
     */
    public MealPlanController() {
        this.db = FirebaseFirestore.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user.getEmail() != null;
        cr = db.collection("User").document(user.getEmail()).collection(collectionName);

    }

    /**
     * Constructor for injecting a db for testing purposes
     * @param db
     */
    public MealPlanController(FirebaseFirestore db) {
        this.db = db;
        this.cr = db.collection("Meal");
    }

    /**
     * Method to add an {@link MealPlan} to the Firebase database
     * @param mealplan This is the {@link MealPlan} to be added to Firebase
     */
    public void addMealPlan(MealPlan mealplan) {
        // get all required values
        String title = mealplan.getName();
        ArrayList<Recipe> recipes = mealplan.getRecipes();
        ArrayList<Ingredient> ingredients = mealplan.getIngredients();
        String startDate = mealplan.getStartDate();
        String endDate = mealplan.getEndDate();

        // put all the values into hashmap
        HashMap<String, Object> data = new HashMap<>();
        data.put("Title", title);
        data.put("Start Date",startDate);
        data.put("End Date", endDate);
        data.put("Ingredients", ingredients);
        data.put("Recipes", recipes);

        // collection reference
        cr
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    /**
                     * Method invoked when successfully added to database
                     * @param documentReference {@link DocumentReference} reference to document
                     */
                    public void onSuccess(DocumentReference documentReference) {
                        String id = documentReference.getId();
                        Log.d("Added", "Added document with ID: "+ id);
                        mealplan.setId(id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    /**
                     * Method invoked when failed to add to database
                     * @param e {@link Exception} the error that occured
                     */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ERROR", "Error adding document", e);
                    }
                });
    }
    /**
     * Method to remove an {@link MealPlan} from Firebase using its ID
     * @param mealplan This is the {@link MealPlan} to be removed from Firebase
     */
    public void removeMealPlan(MealPlan mealplan) {
        String id = mealplan.getId();
        db.collection("MealPlan").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("ContentValues", "Successfully deleted meal plan with ID: " + id);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ContentValues", "Could not delete document with ID: " + id, e);
                    }
                });
    }

    /**
     * Gets all mealplans from Firebase
     *
     * @param s successHandler function to be called on success with
     *          the ArrayList of Recipes
     */
    public void getMealPlan(MealPlanController.successHandler s) {
        cr.get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<MealPlan> meals = new ArrayList<>();

            queryDocumentSnapshots.forEach(doc -> {
                ArrayList<Ingredient> ingredients = new ArrayList<>();
                ArrayList<Recipe> recipes = new ArrayList<>();
                ArrayList<Map<String, Object>> objects1 = (ArrayList<Map<String, Object>>) doc.get("Ingredients");
                objects1.forEach(o -> {
                    ingredients.add(new Ingredient((String) o.get("name"), ((Number) o.get("amount")).doubleValue()));
                });
                ArrayList<Map<String, Object>> objects2 = (ArrayList<Map<String, Object>>) doc.get("Recipes");
                objects2.forEach(o -> {
                    ingredients.add(new Ingredient((String) o.get("name"), (o.get("amount") != null ? (Number) o.get("amount") : 0).doubleValue()));
                });

                MealPlan m = new MealPlan(
                        ingredients,
                        recipes,
                        doc.getString("Title"),
                        doc.getString("Start Date"),
                        doc.getString("End Date")
                );
                m.setId(doc.getId());
                meals.add(m);
            });
            s.f(meals);
        });
    }

    public interface successHandler {
        void f(ArrayList<MealPlan> m);
    }

    /**
     * Notifies the Firestore database of an update to an recipe. The database then updates the
     * recipe's values with the provided {@link MealPlan} object
     * @param mealplan The {@link MealPlan} object to update in the database
     */
    public void notifyUpdate(MealPlan mealplan) {
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("Title",mealplan.getName());
        userMap.put("Ingredients",mealplan.getIngredients());
        userMap.put("Recipes", mealplan.getRecipes());
        userMap.put("Start Start", mealplan.getStartDate());
        userMap.put("End Start", mealplan.getEndDate());
        String id = mealplan.getId();
        cr
                .document(id)
                .update(userMap);
    }

}

