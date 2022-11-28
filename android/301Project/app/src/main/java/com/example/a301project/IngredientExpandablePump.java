package com.example.a301project;

import java.util.ArrayList;
import java.util.HashMap;

public class IngredientExpandablePump {
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
