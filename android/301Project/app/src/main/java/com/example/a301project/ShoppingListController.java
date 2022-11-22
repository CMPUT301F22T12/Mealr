package com.example.a301project;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * This {@link ShoppingListController} class allows the {@link ShoppingListFragment} to communicate with
 * the Firestore database backend. This class contains methods to add or remove {@link Recipe} objects to the
 * database, as well as edit functionality.
 *
 * This class should be used exclusively by the {@link ShoppingListFragment} class to handle database communication.
 */
public class ShoppingListController {
    private final FirebaseFirestore db;
    private final CollectionReference cr;

    /**
     * The constructor for the {@link ShoppingListController}. Sets up the {@link #db} and {@link #cr}
     */
    public ShoppingListController() {
        this.db = FirebaseFirestore.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user.getEmail() != null;
        String collectionName = "Ingredient";
        cr = db.collection("User").document(user.getEmail()).collection(collectionName);
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
