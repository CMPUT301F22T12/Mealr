package com.example.a301project;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class for pumping data into the Ingredient HashMap that maps Ingredient Name (String) as key
 * to an ArrayList<String> containing Ingredient location,category, and expiry date
 */
public class IngredientExpandablePump {
    /**
     *
     * @param ingredients {@Link ArrayList<>} array list containing ingredients
     * @return {@Link HashMap} hash map that maps ingredient names to an array list containing their values
     *
     */
    public static HashMap<String, ArrayList<String>> getData(ArrayList<Ingredient> ingredients) {
        HashMap<String, ArrayList<String>> expandableListDetail = new HashMap<String, ArrayList<String>>();

        for (Ingredient i : ingredients) {
            ArrayList<String> ingredientChildData = new ArrayList<>();
            ingredientChildData.add(i.getLocation());
            ingredientChildData.add(i.getCategory());
            ingredientChildData.add(i.getbbd());

            expandableListDetail.put(i.getName(), ingredientChildData);
        }
        return expandableListDetail;
    }
}
