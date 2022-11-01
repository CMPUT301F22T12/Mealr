package com.example.a301project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecipeController {
    private FirebaseFirestore db;
    private CollectionReference cr;

    public RecipeController() {
        this.db = FirebaseFirestore.getInstance();
        this.cr = db.collection("Recipe");
    }

    public void addRecipe(Recipe recipe) {
        String title = recipe.getTitle();
        String category = recipe.getCategory();
        String comments = recipe.getComments();
        Long prepTime = recipe.getPrepTime();
        Long servings = recipe.getServings();

        String photo = "";
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

        HashMap<String, Object> data = new HashMap<>();
        data.put("Title", title);
        data.put("Category", category);
        data.put("Comments", comments);
        data.put("Ingredients", ingredients);
        data.put("Photo", photo);
        data.put("PrepTime", prepTime);
        data.put("Servings", servings);

        cr
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String id = documentReference.getId();
                        Log.d("Added", "Added document with ID: "+ id);
                        recipe.setId(id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ERROR", "Error adding document", e);
                    }
                });
    }

    /**
     * Gets all recipes from Firebase
     *
     * @param s successHandler function to be called on success with
     *          the ArrayList of Recipes
     */
    public void getRecipes(successHandler s) {
        cr.get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<Recipe> res = new ArrayList<>();

            queryDocumentSnapshots.forEach(doc -> {
                Map<String, Object> data = doc.getData();
                Recipe r = new Recipe(
                        (String) data.get("Title"),
                        (String) data.get("Category"),
                        (String) data.get("Comments"),
                        (String) data.get("Photo"),
                        (Long) data.get("PrepTime"),
                        (Long) data.get("Servings"),
                        new ArrayList<>()
                );
                r.setId(doc.getId());
                res.add(r);
            });
            s.f(res);
        });
    }

    public interface successHandler {
        void f(ArrayList<Recipe> r);
    }

    public void notifyUpdate(Recipe recipe) {
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("Title",recipe.getTitle());
        userMap.put("Category",recipe.getCategory());
        userMap.put("Comments",recipe.getComments());
        userMap.put("Ingredients",recipe.getIngredients());
        userMap.put("Photo", recipe.getPhoto());
        userMap.put("Servings", recipe.getServings());
        userMap.put("PrepTime", recipe.getPrepTime());
        String id = recipe.getId();
        db.collection("Recipe")
                .document(id)
                .update(userMap);
    }
}
