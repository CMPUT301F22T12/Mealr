package com.example.a301project;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class ShoppingListController {
    private FirebaseFirestore db;
    private CollectionReference cr;

    public ShoppingListController() {
        this.db = FirebaseFirestore.getInstance();
        this.cr = db.collection("Ingredient");
    }

    public interface successHandler {
        void f(ArrayList<ShoppingItem> r);
    }

    /**
     * Gets all ingredients from Firebase and filter ones reqruied
     *
     * @param s successHandler function to be called on success with
     *          the ArrayList of Recipes
     */
    public void getShoppingItems(ShoppingListController.successHandler s) {
        cr.get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<ShoppingItem> res = new ArrayList<>();

            queryDocumentSnapshots.forEach(doc -> {
                Map<String, Object> data = doc.getData();
                ShoppingItem item = new ShoppingItem(
                        (String) data.get("Name"),
                        Math.toIntExact((Long) data.get("Amount")),
                        (String) data.get("Unit"),
                        (String) data.get("Category")
                );
                res.add(item);
            });
            s.f(res);
        });
    }
}
