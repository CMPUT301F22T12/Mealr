package com.example.a301project;

import android.util.Log;

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
     * Gets all ingredients from Firebase and filter ones
     *
     * @param s successHandler function to be called on success with
     *          the ArrayList of Recipes
     */
    public void getShoppingItems(ShoppingListController.successHandler s) {
        cr.get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<ShoppingItem> res = new ArrayList<>();


            queryDocumentSnapshots.forEach(doc -> {
                ShoppingItem item = new ShoppingItem(
                        doc.getString("Name"),
                        doc.getDouble("Amount"),
                        doc.getString("Unit"),
                        doc.getString("Category")
                );
                res.add(item);
            });
            s.f(res);
        });
    }
}
