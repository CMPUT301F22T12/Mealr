package com.example.a301project;

import java.io.Serializable;

/**
 * This is a class that holds the data which makes up an ingredient. This class consists mostly of
 * getters and setters to make retrieving its parameters easier.
 */
@SuppressWarnings("SpellCheckingInspection")
public class Ingredient implements Serializable {
    private String id = null;
    private String name;
    private String location;
    private String bbd;
    private String category;
    private String unit;
    private double amount;

    /**
     * Makes an {@link Ingredient} object from the given parameters
     * @param name {@link String} The name of the object
     * @param amount {@link Integer} The amount of the object
     * @param bbd {@link String} The best before date of the object
     * @param location {@link String} The location of the object
     * @param unit {@link String} The unit the object uses
     * @param category {@link String} The object's category
     */
    public Ingredient(String name, double amount, String bbd, String location, String unit, String category) {
        // constructor
        this.name = name;
        this.location = location;
        this.bbd = bbd;
        this.category = category;
        this.unit = unit;
        this.amount = amount;
    }

    public Ingredient(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    /**
     * Gets the {@link Ingredient} object's ID
     * @return The ID of the object
     */
    public String getId() {return this.id;}

    /**
     * Gets the {@link Ingredient} object's name
     * @return The name of the object
     */
    public String getName() {return this.name;}

    /**
     * Gets the {@link Ingredient} object's best before date
     * @return The best before date of the object
     */
    public String getbbd() {return this.bbd;}

    /**
     * Gets the {@link Ingredient} object's location
     * @return The location of the object
     */
    public String getLocation() {return this.location;}

    /**
     * Gets the {@link Ingredient} object's amount
     * @return The amount of the object (as an {@link Double}
     */
    public Double getAmount() {return this.amount;}

    /**
     * Gets the {@link Ingredient} object's unit
     * @return The unit of the object
     */
    public String getUnit() {return this.unit;}

    /**
     * Gets the {@link Ingredient} object's category
     * @return The category of the object
     */
    public String getCategory() {return this.category;}

    // setters used to edit attributes for existing food object

    /**
     * Sets the {@link Ingredient} object's ID
     * @param id The {@link Ingredient} object's ID is set to this value
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the {@link Ingredient} object's name
     * @param name The {@link Ingredient} object's name is set to this value
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the {@link Ingredient} object's best before date
     * @param bbd The {@link Ingredient} object's best before date is set to this value
     */
    public void setBbd(String bbd) {
        this.bbd = bbd;
    }

    /**
     * Sets the {@link Ingredient} object's location
     * @param location The {@link Ingredient} object's location is set to this value
     */
    public void setLocation(String location) {this.location = location;}

    /**
     * Sets the {@link Ingredient} object's unit
     * @param unit {@link String }The unit of the {@link Ingredient}
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Sets the {@link Ingredient} object's amount (as an double)
     * @param amount The {@link Ingredient} object's amount is set to this value
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Sets the {@link Ingredient} object's category
     * @param category The {@link Ingredient} object's category is set to this value
     */
    public void setCategory(String category) {
        this.category = category;
    }
}

