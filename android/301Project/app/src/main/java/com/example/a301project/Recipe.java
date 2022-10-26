package com.example.a301project;

import java.util.ArrayList;

public class Recipe {
    private String title;
    private String category;
    private String comments;
    private String photo;
    private Integer prepTime;
    private Integer servings;
    private ArrayList<Ingredient> ingredients;

    public Recipe(String title, String category, String comments, String photo, Integer prepTime, Integer servings, ArrayList<Ingredient> ingredients) {
        this.title = title;
        this.category = category;
        this.comments = comments;
        this.photo = photo;
        this.prepTime = prepTime;
        this.servings = servings;
        this.ingredients = ingredients;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
