package com.example.a301project;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * This {@link ShoppingListController} class allows the {@link ShoppingListFragment} to communicate with
 * the Firestore database backend. This class contains methods to add or remove {@link Recipe} objects to the
 * database, as well as edit functionality.
 *
 * This class should be used exclusively by the {@link ShoppingListFragment} class to handle database communication.
 */
public class ShoppingListController {
    private final String collectionName = "ShoppingList";
    private final FirebaseFirestore db;
    private CollectionReference ingredient_cr;
    private CollectionReference mealplan_cr;
    private CollectionReference recipe_cr;
    private ArrayList<ShoppingItem> mealPlanItemsDataList;
    private ArrayList<Recipe> mealPlanRecipesDataList;
    private ArrayList<ShoppingItem> ingredientStorageItemsDataList;
    private ArrayList<Recipe> recipeItemsDataList;
    /**
     * The constructor for the {@link ShoppingListController}. Sets up the {@link #db} and {@link #ingredient_cr}
     */
    public ShoppingListController() {
        this.db = FirebaseFirestore.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user.getEmail() != null;
        String ingredientCollectionName = "Ingredient";
        ingredient_cr = db.collection("User").document(user.getEmail()).collection(ingredientCollectionName);
        String recipeCollectionName = "Recipe";
        recipe_cr = db.collection("User").document(user.getEmail()).collection(recipeCollectionName);
        String mealplanCollectionName = "MealPlan";
        mealplan_cr = db.collection("User").document(user.getEmail()).collection(mealplanCollectionName);
    }

    /**
     * Constructor for injecting a db for testing purposes
     * @param db the database
     */
    public ShoppingListController(FirebaseFirestore db) {
        this.db = db;
        //mealplan_cr = db.collection(collectionName);
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
     * A simple listener for when the Recipes is done being read from Firebase
     */
    public interface recipeItemSuccessHandler {
        void r(ArrayList<Recipe> r);
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

            ArrayList<ShoppingItem> shoppingItemDataList = new ArrayList<>();
            // get all the ingredient from the recipes in the users meal plans
            getIngredientsFromMealPlanRecipes();

            // if there actually is ShoppingItems in the MealPlan
            if (mealPlanItemsDataList != null) {
                // loop through each ShoppingItem from MealPlan
                for (int i = 0; i < mealPlanItemsDataList.size(); i++) {
                    ShoppingItem item = mealPlanItemsDataList.get(i);
                    boolean matches = false;
                    boolean enoughIngredient = false;
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
                                // then item does not need to be added to shopping list
                                mealPlanItemsDataList.remove(item);
                                enoughIngredient = true;
                                i--;
                                break;
                            } else {
                                // if there isn't enough of the Ingredient in the Storage
                                amount1-=amount2;
                                item.setAmount(amount1);
                                item2.setAmount(0.0);
                            }
                        }
                    }
                    // if the ShoppingItem from MealPlan was not in IngredientStorage -> then add it to ShoppingList
                    if (!matches) {
                        // check if there is already an item with that name in the shopping list
                        boolean nameMatch = false;
                        for (int k = 0; k < shoppingItemDataList.size(); k++) {
                            ShoppingItem item3 = shoppingItemDataList.get(k);
                            if (item3.getName().equalsIgnoreCase(item.getName())) {
                                // then combine the amount
                                item3.setAmount(item3.getAmount() + item.getAmount());
                                shoppingItemDataList.set(k, new ShoppingItem(
                                        item3.getName(),
                                        item3.getAmount(),
                                        item3.getUnit(),
                                        item3.getCategory()
                                ));
                                nameMatch = true;
                            }
                        }
                        // if there isn't already that ingredient in the shopping list then add a new one
                        if (!nameMatch) {
                            shoppingItemDataList.add(new ShoppingItem(
                                    item.getName(),
                                    item.getAmount(),
                                    item.getUnit(),
                                    item.getCategory()
                            ));
                        }
                    } else {
                        if (!enoughIngredient) {
                            // also if there is not enough of the Ingredient in the Storage then add the difference to the ShoppingList
                            ShoppingItem newItem = new ShoppingItem(
                                    item.getName(),
                                    item.getAmount(),
                                    item.getUnit(),
                                    item.getCategory()
                            );
                            // check if that ingredient is already in the shopping list
                            boolean nameMatch = false;
                            for (int k = 0; k < shoppingItemDataList.size(); k++) {
                                ShoppingItem item3 = shoppingItemDataList.get(k);
                                if (item3.getName().equalsIgnoreCase(newItem.getName())) {
                                    // then combine the amount (combine the ingredient and the shopping list item)
                                    item3.setAmount(item3.getAmount() + newItem.getAmount());
                                    shoppingItemDataList.set(k, new ShoppingItem(
                                            item3.getName(),
                                            item3.getAmount(),
                                            item3.getUnit(),
                                            item3.getCategory()
                                    ));
                                    nameMatch = true;
                                }
                            }
                            // if the ingredient wasn't already in shoppinglist -> add it
                            if (!nameMatch) {
                                shoppingItemDataList.add(newItem);
                            }
                        }
                    }
                }
            }
            s.f(shoppingItemDataList);
    }

    /**
     * From the recipes read from Firebase -> go through all of them and check if they are in MealPlan
     * If the recipe is in meal plan add it's Ingredient to mealPlanItemsDataList (which is the list of needed Ingredients)
     */
    private void getIngredientsFromMealPlanRecipes() {

        // loop through mealplanrecipes and get all the ingredients currently in the recipe and multiply it by serving#
        if (mealPlanRecipesDataList != null) {
            mealPlanRecipesDataList.forEach(recipe -> {
                Long servings = recipe.getServings();
                String title = recipe.getTitle();

                // loop through the actual recipes and look for a match
                for (int i = 0; i < recipeItemsDataList.size(); i++) {
                    if (recipeItemsDataList.get(i).getTitle().equalsIgnoreCase(title)) {
                        // found a match -> get all ingredients in the recipe
                        ArrayList<Ingredient> ingredients = recipeItemsDataList.get(i).getIngredients();

                        // add each ingredient to the mealPlanItemsDataList with the correct serving
                        if (ingredients != null) {
                            for (int j = 0; j < ingredients.size(); j++) {
                                Ingredient ingredient = ingredients.get(j);
                                mealPlanItemsDataList.add(new ShoppingItem(
                                        ingredient.getName(),
                                        ingredient.getAmount() * servings.doubleValue(),
                                        ingredient.getUnit(),
                                        ingredient.getCategory()
                                ));
                            }
                        }
                    }
                }
            });
        }
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
            mealPlanRecipesDataList = new ArrayList<>();

            // for each MealPlan read from Firebase
            queryDocumentSnapshots.forEach(doc -> {
                // get the Ingredients list from the MealPlan
                ArrayList<HashMap<String, Object>> values = (ArrayList<HashMap<String, Object>>) doc.get("Ingredients");
                if (values != null) {
                    // if there is Ingredients then create an equivalent ShoppingItem for each
                    for (int i = 0; i < values.size(); i++) {
                        HashMap<String,Object> value = values.get(i);
                        double amount = 0;
                        try {
                            amount = ((Long) value.get("amount")).doubleValue();
                        } catch (Exception e) {
                            amount = (double) value.get("amount");
                        }
                        ShoppingItem ingredient = new ShoppingItem(
                                String.valueOf(value.get("name")),
                                amount,
                                String.valueOf(value.get("unit")),
                                String.valueOf(value.get("category"))
                        );
                        mealPlanItemsDataList.add(ingredient);
                    }
                }

                // get the Recipes list from the MealPlan
                ArrayList<HashMap<String, Object>> recipes = (ArrayList<HashMap<String, Object>>) doc.get("Recipes");
                if (recipes != null) {
                    // if there is some Recipes then loop through one-by-one
                    for (int j = 0; j < recipes.size(); j++) {
                        HashMap<String, Object> recipe = recipes.get(j);
                        // for each Recipe, get the list of Ingredients
                        mealPlanRecipesDataList.add(new Recipe(
                                String.valueOf(recipe.get("title")),
                                null,
                                null,
                                null,
                                null,
                                (Long) recipe.get("servings"),
                                null
                        ));
                    }
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

    /**
     * Reads all the {@link Recipe} from Firebase
     * @param s successHandler function to be called on success with
     *          the ArrayList of Shopping Items
     */
    public void getRecipeItems(ShoppingListController.recipeItemSuccessHandler s) {
        recipe_cr.get().addOnSuccessListener(queryDocumentSnapshots -> {
            recipeItemsDataList = new ArrayList<>();

            // for each Recipe that is read
            queryDocumentSnapshots.forEach(doc -> {
                String id = doc.getId();
                ArrayList<Ingredient> ingredients = new ArrayList<>();
                // get the Ingredients for that recipe
                ArrayList<HashMap<String, Object>> recipeIngredients = (ArrayList<HashMap<String, Object>>) doc.get("Ingredients");
                if (recipeIngredients != null) {
                    // for each ingredient read from the recipe -> created an Ingredient Object
                    for (int i = 0; i < recipeIngredients.size(); i++) {
                        HashMap<String, Object> rIngredient = recipeIngredients.get(i);
                        double amount = 0;
                        try {
                            amount = ((Long) rIngredient.get("amount")).doubleValue();
                        } catch (Exception e) {
                            amount = (double) rIngredient.get("amount");
                        }
                        Ingredient ingredient = new Ingredient(
                                String.valueOf(rIngredient.get("name")),
                                amount,
                                String.valueOf(rIngredient.get("bbd")),
                                String.valueOf(rIngredient.get("location")),
                                String.valueOf(rIngredient.get("unit")),
                                String.valueOf(rIngredient.get("category"))
                        );
                        ingredients.add(ingredient);
                    }
                }
                // create a Recipe object for each recipe read from Firebase
                Recipe recipe = new Recipe(
                        doc.getString("Title"),
                        doc.getString("Category"),
                        doc.getString("Comments"),
                        doc.getString("Photo"),
                        doc.get("PrepTime", Long.class),
                        doc.get("Servings", Long.class),
                        ingredients
                );
                recipe.setId(id);
                recipeItemsDataList.add(recipe);
            });
            s.r(recipeItemsDataList);
        });
    }
}
