package com.example.a301project;

import java.util.ArrayList;

public class Recipe {
    private String title;
    private String category;
    private String comments;
    private String photo;
    private Long prepTime;
    private Long servings;
    private ArrayList<Ingredient> ingredients;

    public Recipe(String title, String category, String comments, String photo, Long prepTime, Long servings, ArrayList<Ingredient> ingredients) {
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

    public Long getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Long prepTime) {
        this.prepTime = prepTime;
    }

    public Long getServings() {
        return servings;
    }

    public void setServings(Long servings) {
        this.servings = servings;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
