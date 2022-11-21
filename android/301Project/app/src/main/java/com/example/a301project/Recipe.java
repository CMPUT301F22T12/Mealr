package com.example.a301project;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is a class that holds the data which makes up a Recipe. This class consists mostly of
 * getters and setters to make retrieving its parameters easier.
 */
public class Recipe implements Serializable {
    private String title;
    private String category;
    private String comments;
    private String photo;
    private Long prepTime;
    private Long servings;
    private ArrayList<Ingredient> ingredients;
    private String id = null;

    /**
     * Constructor for a {@link Recipe} object with its parameters
     * @param title {@link String}
     * @param category {@link String}
     * @param comments {@link String}
     * @param photo {@link String}
     * @param prepTime {@link String}
     * @param servings {@link String}
     * @param ingredients {@link String}
     */
    public Recipe(String title, String category, String comments, String photo, Long prepTime, Long servings, ArrayList<Ingredient> ingredients) {
        this.title = title;
        this.category = category;
        this.comments = comments;
        this.photo = photo;
        this.prepTime = prepTime;
        this.servings = servings;
        this.ingredients = ingredients;
    }

    public Recipe(String name, Long servings) {
        this.title = title;
        this.servings = servings;
    }

    /**
     * gets the {@link Recipe} title
     * @return {@link String}title of object
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the {@link Recipe} title as the one passed in
     * @param title {@link String} title of recipe
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * gets the category of a {@link Recipe} object
     * @return {@link String} category of Recipe
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the {@link Recipe}category as the one passed in
     * @param category {@link String} category of recipe
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * gets the comments of a {@link Recipe} object
     * @return {@link String} comments of Recipe
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the {@link Recipe} comments as the one passed in
     * @param comments {@link String} comments of recipe
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * gets the URL of the photo of {@link Recipe} object
     * @return {@link String} URL of photo of Recipe
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * Sets the {@link Recipe} photo as the one passed in
     * @param photo {@link String} URL address of photo of recipe
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * gets the prep time of {@link Recipe} object
     * @return {@link Long} prep time of photo of Recipe
     */
    public Long getPrepTime() {
        return prepTime;
    }

    /**
     * Sets the {@link Recipe} prep time as the one passed in
     * @param prepTime {@link Long} prep time of photo of recipe
     */
    public void setPrepTime(Long prepTime) {
        this.prepTime = prepTime;
    }

    /**
     * gets the servings of {@link Recipe} object
     * @return {@link Long} servings of photo of Recipe
     */
    public Long getServings() {
        return servings;
    }

    /**
     * Sets the {@link Recipe} servings as the one passed in
     * @param servings {@link Long} servings of photo of recipe
     */
    public void setServings(Long servings) {
        this.servings = servings;
    }

    /**
     * gets the ingredients of {@link Recipe} object
     * @return {@link ArrayList} ingredients array of Recipe
     */
    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Sets the {@link Recipe} ingredient array as the one passed in
     * @param ingredients {@link ArrayList} servings of photo of recipe
     */
    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
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
}
