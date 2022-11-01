package com.example.a301project;

import java.util.ArrayList;

/**
 * Represents an individual meal plan with ingredients and recipes.
 * MealPlanActivity contains multiple instances of these.
 */
public class MealPlan {
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Recipe> recipes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public MealPlan(ArrayList<Ingredient> ingredients, ArrayList<Recipe> recipes, String name) {
        this.ingredients = ingredients;
        this.recipes = recipes;
        this.name = name;
    }

    public MealPlan(String name) {
        this(new ArrayList<>(), new ArrayList<>(), name);
    }

}
