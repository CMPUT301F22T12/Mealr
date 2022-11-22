package com.example.a301project;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents an individual meal plan with ingredients and recipes.
 * MealPlanFragment contains multiple instances of these.
 */
public class MealPlan implements Serializable {
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Recipe> recipes;
    private String startDate;
    private String endDate;
    private String name;
    private String id = null;

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
    public void setStartDate(String startDate) {this.startDate = startDate;}
    public void setEndDate(String endDate) {this.endDate = endDate;}
    public String getStartDate() {return startDate;}
    public String getEndDate() {return endDate;}

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Sets the {@link MealPlan} ingredient array as the one passed in
     * @param ingredients {@link ArrayList} servings of photo of recipe
     */
    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    /**
     * Sets the {@link MealPlan} ingredient array as the one passed in
     * @param recipes {@link ArrayList} servings of photo of recipe
     */
    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    /**
     * Sets the {@link Recipe} ID as the one passed in
     * @param id {@link String} ID of recipe
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * gets the ID of {@link Recipe} object
     * @return {@link String} ID of Recipe
     */
    public String getId() {
        return this.id;
    }


    /**
     * Constructor for an MealPlan's attributes containing ingredients and recipes
     * @param ingredients {@link Ingredient}
     * @param recipes {@link Recipe}
     * @param name {@link String}
     */
    public MealPlan(ArrayList<Ingredient> ingredients, ArrayList<Recipe> recipes, String name, String startDate, String endDate) {
        this.ingredients = ingredients;
        this.recipes = recipes;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }


}
