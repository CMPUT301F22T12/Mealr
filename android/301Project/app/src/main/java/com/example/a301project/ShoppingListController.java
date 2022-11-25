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
    private final CollectionReference ingredient_cr;
    private final CollectionReference mealplan_cr;
    private ArrayList<ShoppingItem> mealPlanItemsDataList;
    private ArrayList<ShoppingItem> ingredientStorageItemsDataList;
    /**
     * The constructor for the {@link ShoppingListController}. Sets up the {@link #db} and {@link #ingredient_cr}
     */
    public ShoppingListController() {
        this.db = FirebaseFirestore.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user.getEmail() != null;
        String ingredientCollectionName = "Ingredient";
        ingredient_cr = db.collection("User").document(user.getEmail()).collection(ingredientCollectionName);
        String mealplanCollectionName = "MealPlan";
        mealplan_cr = db.collection("User").document(user.getEmail()).collection(mealplanCollectionName);
    }

    public interface shoppingItemSuccessHandler {
        void f(ArrayList<ShoppingItem> r);
    }

    public interface ingredientItemSuccessHandler {
        void f(ArrayList<ShoppingItem> r);
    }

    public interface mealPlanSuccessHandler {
        void f(ArrayList<ShoppingItem> r);
    }


    /**
     * Gets all ingredients from Firebase and filter ones
     *
     * @param s successHandler function to be called on success with
     *          the ArrayList of Recipes
     */
    public void getShoppingItems(ShoppingListController.shoppingItemSuccessHandler s) {
        ingredient_cr.get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<ShoppingItem> shoppingItemDataList = new ArrayList<>();

            getIngredientStorageItems(res -> setIngredientStorageItemsDataList(res));
            getMealPlanItems(res -> setMealItemDataList(res));

            mealPlanItemsDataList.forEach(item -> {
                ingredientStorageItemsDataList.forEach(item2 -> {
                    if(item.getName().equalsIgnoreCase(item2.getName())) {
                        double amount1 = item.getAmount();
                        double amount2 = item2.getAmount();
                        if (amount2 >= amount1) {
                            item2.setAmount(amount2-amount1);
                            amount1 = 0;
                            mealPlanItemsDataList.remove(item);
                        } else {
                            amount1-=amount2;
                            ingredientStorageItemsDataList.remove(item2);
                            ShoppingItem newItem = new ShoppingItem(
                                    item.getName(),
                                    amount1,
                                    item.getUnit(),
                                    item.getCategory()
                            );
                            shoppingItemDataList.add(newItem);
                        }
                    }
                });
            });
            s.f(shoppingItemDataList);
        });
    }

    /**
     * Gets all ingredients from MealPlan in Firebase
     * @param s successHandler function to be called on success with
     *          the ArrayList of Shopping Item
     */
    public void getMealPlanItems(ShoppingListController.mealPlanSuccessHandler s) {
        ingredient_cr.get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<ShoppingItem> res = new ArrayList<>();

            queryDocumentSnapshots.forEach(doc -> {
                ArrayList<Ingredient> ingredients = (ArrayList<Ingredient>) doc.get("Ingredients");
                ingredients.forEach(ingredient -> {
                    ShoppingItem item = new ShoppingItem(
                            ingredient.getName(),
                            ingredient.getAmount(),
                            ingredient.getUnit(),
                            ingredient.getCategory()
                    );
                    res.add(item);
                });
                ArrayList<Recipe> recipes = (ArrayList<Recipe>) doc.get("Recipes");
                recipes.forEach(recipe -> {
                    ArrayList<Ingredient> recipeIngredients = recipe.getIngredients();
                    recipeIngredients.forEach(rIngredient -> {
                        ShoppingItem item2 = new ShoppingItem(
                                rIngredient.getName(),
                                rIngredient.getAmount(),
                                rIngredient.getUnit(),
                                rIngredient.getCategory()
                        );
                        res.add(item2);
                    });

                });

            });
            s.f(res);
        });
    }

    /**
     * Gets all ingredients from Firebase and filter ones
     *
     * @param s successHandler function to be called on success with
     *          the ArrayList of Shopping Items
     */
    public void getIngredientStorageItems(ShoppingListController.ingredientItemSuccessHandler s) {
        ingredient_cr.get().addOnSuccessListener(queryDocumentSnapshots -> {
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

    /**
     * Sets the internal meal plan shopping items
     * @param a ArrayList of shopping items to set the data list to
     */
    private void setMealItemDataList(ArrayList<ShoppingItem> a) {
        mealPlanItemsDataList.clear();
        mealPlanItemsDataList.addAll(a);
    }

    /**
     * Sets the internal ingredient storage shopping item
     * @param a ArrayList of shopping items to set the data list to
     */
    private void setIngredientStorageItemsDataList(ArrayList<ShoppingItem> a) {
        ingredientStorageItemsDataList.clear();
        ingredientStorageItemsDataList.addAll(a);
    }

}
