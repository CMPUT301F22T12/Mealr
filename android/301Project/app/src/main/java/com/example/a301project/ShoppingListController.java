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

                // Same fix as with the ingredient controller.
                // TODO: Figure out a way to not use try except for this conversion
                Double amount;
                try {
                    Long amountL = (Long) doc.getData().get("Amount");
                    amount = amountL.doubleValue();
                }
                catch(ClassCastException e) {
                    amount = (Double) doc.getData().get("Amount");
                }

                ShoppingItem item = new ShoppingItem(
                        (String) data.get("Name"),
                        amount,
                        (String) data.get("Unit"),
                        (String) data.get("Category")
                );
                res.add(item);
            });
            s.f(res);
        });
    }
}
