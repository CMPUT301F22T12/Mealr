package com.example.a301project;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class RecipeController {
    private FirebaseFirestore db;
    private CollectionReference cr;

    public RecipeController() {
        this.db = FirebaseFirestore.getInstance();
        this.cr = db.collection("Recipe");
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
                res.add(r);
            });
            s.f(res);
        });
    }

    public interface successHandler {
        void f(ArrayList<Recipe> r);
    }
}
