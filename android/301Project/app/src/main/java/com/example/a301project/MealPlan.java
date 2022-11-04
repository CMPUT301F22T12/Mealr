package com.example.a301project;

import java.util.ArrayList;

/**
 * Represents an individual meal plan with ingredients and recipes.
 * MealPlanActivity contains multiple instances of these.
 */
public class MealPlan {
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Recipe> recipes;

    /**
     * Gets the {@link MealPlan} object's name
     * @return The name of the object
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the {@link MealPlan} object's name
     * @param name The {@link MealPlan} object's name is set to this value
     */
    public void setName(String name) {
        this.name = name;
    }

    private String name;

    /**
     * Constructor for an MealPlan's attributes containing ingredients and recipes
     * @param ingredients {@link Ingredient}
     * @param recipes {@link Recipe}
     * @param name {@link String}
     */
    public MealPlan(ArrayList<Ingredient> ingredients, ArrayList<Recipe> recipes, String name) {
        this.ingredients = ingredients;
        this.recipes = recipes;
        this.name = name;
    }

    /**
     * Constructor for MealPlan that only takes a name
     * creates ArrayLists as new
     * @param name {@String}
     */
    public MealPlan(String name) {
        this(new ArrayList<>(), new ArrayList<>(), name);
    }

}
