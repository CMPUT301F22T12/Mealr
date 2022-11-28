package com.example.a301project;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class for pumping data into the Recipe HashMap that maps Recipe Name (String) as key
 * to an ArrayList<String> containing Recipe prep-time, servings, and category
 */
public class RecipeExpandablePump {

    /**
     *
     * @param recipes {@Link ArrayList<>} array list containing recipe
     * @return {@Link HashMap} hash map that maps recipe names to an array list containing their values
     *
     */
    public static HashMap<String, ArrayList<String>> getData(ArrayList<Recipe> recipes) {
        HashMap<String, ArrayList<String>> expandableListDetail = new HashMap<String, ArrayList<String>>();

        for (Recipe r : recipes) {
            ArrayList<String> recipeChildData = new ArrayList<>();
            recipeChildData.add(r.getPrepTime() + " min");
            recipeChildData.add(r.getCategory());
            recipeChildData.add(r.getServings().toString()+" servings ");

            expandableListDetail.put(r.getTitle(), recipeChildData);
        }
        return expandableListDetail;
    }
}
