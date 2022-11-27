package com.example.a301project;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This {@link ShoppingListController} class allows the {@link ShoppingListFragment} to communicate with
 * the Firestore database backend. This class contains methods to add or remove {@link Recipe} objects to the
 * database, as well as edit functionality.
 *
 * This class should be used exclusively by the {@link ShoppingListFragment} class to handle database communication.
 */
public class ShoppingListController {
    private final FirebaseFirestore db;
    private CollectionReference ingredient_cr;
    private final String collectionName = "ShoppingList";
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

    /**
     * Constructor for injecting a db for testing purposes
     * @param db the database
     */
    public ShoppingListController(FirebaseFirestore db) {
        this.db = db;
        mealplan_cr = db.collection(collectionName);
    }



    /**
     * A simple listener for when the calculated list of {@link ShoppingItem} is completed
     */
    public interface shoppingItemSuccessHandler {
        void f(ArrayList<ShoppingItem> r);
    }

    /**
     * A simple listener for when the Ingredients is done being read from Firebase
     */
    public interface ingredientItemSuccessHandler {
        void f(ArrayList<ShoppingItem> r);
    }

    /**
     * A simple listener for when the MealPlan is done being read from Firebase
     */
    public interface mealPlanSuccessHandler {
        void f(ArrayList<ShoppingItem> r);
    }


    /**
     * From the {@link Ingredient} read from Firebase, calculate the {@link ShoppingItem}
     * that the user needs to purchase
     * This is done by looping through each Ingredient from the MealPlans and checking if
     * it exists it the IngredientStorage (and compares the amounts)
     * @param s successHandler function to be called on success with
     *          the ArrayList of ShoppingItem
     */
    public void getShoppingItems(ShoppingListController.shoppingItemSuccessHandler s) {
        ingredient_cr.get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<ShoppingItem> shoppingItemDataList = new ArrayList<>();

            // if there actually is ShoppingItems in the MealPlan
            if (mealPlanItemsDataList != null) {
                // loop through each ShoppingItem from MealPlan
                for (int i = 0; i < mealPlanItemsDataList.size(); i++) {
                    ShoppingItem item = mealPlanItemsDataList.get(i);
                    boolean matches = false;
                    // then loop through each ShoppingItem from IngredientStorage
                    for (int j = 0; j < ingredientStorageItemsDataList.size(); j++) {
                        // compare the MealPlan shoppingItem to all the items from the IngredientStorage
                        ShoppingItem item2 = ingredientStorageItemsDataList.get(j);
                        // if the MealPlan item is found in the IngredientStorage
                        if(item.getName().equalsIgnoreCase(item2.getName())) {
                            matches = true;
                            double amount1 = item.getAmount();
                            double amount2 = item2.getAmount();
                            item.setUnit(item2.getUnit());
                            item.setCategory(item2.getCategory());
                            // if there is enough of the Ingredient in the Storage
                            if (amount2 >= amount1) {
                                item2.setAmount(amount2-amount1);
                                amount1 = 0;
                                // then item does not need to be added to shopping list
                                mealPlanItemsDataList.remove(item);
                                i--;
                            } else {
                                // if there isn't enough of the Ingredient in the Storage
                                amount1-=amount2;
                                item.setAmount(amount1);
                                ingredientStorageItemsDataList.remove(item2);
                                j--;
                            }
                        }
                    }
                    // if the ShoppingItem from MealPlan was not in IngredientStorage -> then add it to ShoppingList
                    if (!matches) {
                        shoppingItemDataList.add(item);
                    } else {
                        if (mealPlanItemsDataList.contains(item)) {
                            // also if there is not enough of the Ingredient in the Storage then add the difference to the ShoppingList
                            ShoppingItem newItem = new ShoppingItem(
                                    item.getName(),
                                    item.getAmount(),
                                    item.getUnit(),
                                    item.getCategory()
                            );
                            shoppingItemDataList.add(newItem);
                        }
                    }
                }
            }
            s.f(shoppingItemDataList);
        });
    }

    /**
     * Reads all the {@link MealPlan} from Firebase and then reads all the {@link Ingredient}
     * from each {@link MealPlan} and creates a ShoppingItem for each
     * @param s successHandler function to be called on success with
     *          the ArrayList of Shopping Item
     */
    public void getMealPlanItems(ShoppingListController.mealPlanSuccessHandler s) {
        mealplan_cr.get().addOnSuccessListener(queryDocumentSnapshots -> {
            mealPlanItemsDataList = new ArrayList<>();

            // for each MealPlan read from Firebase
            queryDocumentSnapshots.forEach(doc -> {
                // get the Ingredients list from the MealPlan
                ArrayList<HashMap<String, Object>> values = (ArrayList<HashMap<String, Object>>) doc.get("Ingredients");
                if (values != null) {
                    // if there is Ingredients then create an equivalent ShoppingItem for each
                    values.forEach(value -> {
                        ShoppingItem ingredient = new ShoppingItem(
                                String.valueOf(value.get("name")),
                                (double) value.get("amount"),
                                String.valueOf(value.get("unit")),
                                String.valueOf(value.get("category"))
                        );
                        mealPlanItemsDataList.add(ingredient);
                    });
                }

                // get the Recipes list from the MealPlan
                ArrayList<HashMap<String, Object>> recipes = (ArrayList<HashMap<String, Object>>) doc.get("Recipes");
                if (recipes != null) {
                    // if there is some Recipes then loop through one-by-one
                    recipes.forEach(recipe -> {
                        // for each Recipe, get the list of Ingredients
                        ArrayList<HashMap<String, Object>> recipeIngredients = (ArrayList<HashMap<String, Object>>) recipe.get("ingredients");
                        if (recipeIngredients != null) {
                            // if there is Ingredients then create an equivalent shoppingItem for each
                            recipeIngredients.forEach(rIngredient -> {
                                ShoppingItem item2 = new ShoppingItem(
                                        String.valueOf(rIngredient.get("name")),
                                        (double) rIngredient.get("amount"),
                                        String.valueOf(rIngredient.get("unit")),
                                        String.valueOf(rIngredient.get("category"))
                                );
                                mealPlanItemsDataList.add(item2);
                            });
                        }
                    });
                }
            });
            s.f(mealPlanItemsDataList);
        });
    }

    /**
     * Reads all the {@link Ingredient} from Firebase and creates a ShoppingItem for each
     * @param s successHandler function to be called on success with
     *          the ArrayList of Shopping Items
     */
    public void getIngredientStorageItems(ShoppingListController.ingredientItemSuccessHandler s) {
        ingredient_cr.get().addOnSuccessListener(queryDocumentSnapshots -> {
            ingredientStorageItemsDataList = new ArrayList<>();

            // for each Ingredient that is read -> create an equivalent (simplified) ShoppingItem
            queryDocumentSnapshots.forEach(doc -> {
                ShoppingItem item = new ShoppingItem(
                        doc.getString("Name"),
                        doc.getDouble("Amount"),
                        doc.getString("Unit"),
                        doc.getString("Category")
                );
                ingredientStorageItemsDataList.add(item);
            });
            s.f(ingredientStorageItemsDataList);
        });
    }
}
