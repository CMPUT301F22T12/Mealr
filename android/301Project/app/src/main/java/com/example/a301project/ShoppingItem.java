package com.example.a301project;

/**
 * This is a class that holds the data which makes up a Shopping. This class consists mostly of
 *  getters and setters to make retrieving its parameters easier.
 *  This class extends Ingredient class because Shopping List acts as a container for Ingredient Objects
 */
public class ShoppingItem{

    private String name;
    private double amount;
    private String unit;
    private String category;

    /**
     * Makes an {@link ShoppingItem} object from the given parameters
     * and uses an Ingredient object to store the values
     * @param name     The name of the object
     * @param amount   The amount of the object
     * @param unit     The unit the object uses
     * @param category The object's category
     */
    public ShoppingItem(String name, double amount, String unit, String category) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }

    /**
     * Gets the {@link ShoppingItem} object's name
     * @return The name of the object
     */
    public String getName() {return this.getName();}

    /**
     * Gets the {@link ShoppingItem} object's amount
     * @return The amount of the object (as an {@link Double}
     */
    public Double getAmount() {return this.getAmount();}

    /**
     * Gets the {@link ShoppingItem} object's unit
     * @return The unit of the object
     */
    public String getUnit() {return this.getUnit();}

    /**
     * Gets the {@link ShoppingItem} object's category
     * @return The category of the object
     */
    public String getCategory() {return this.getCategory();}

    // setters used to edit attributes for existing food object

    /**
     * Sets the {@link ShoppingItem} object's name
     * @param name The {@link ShoppingItem} object's name is set to this value
     */
    public void setName(String name) {
        this.setName(name);
    }

    /**
     * Sets the {@link ShoppingItem} object's unit
     * @param unit {@link String }The unit of the {@link ShoppingItem}
     */
    public void setUnit(String unit) {
        this.setUnit(unit);
    }

    /**
     * Sets the {@link ShoppingItem} object's amount (as an integer)
     * @param amount The {@link ShoppingItem} object's amount is set to this value
     */
    public void setAmount(Double amount) {
        this.setAmount(amount);
    }

    /**
     * Sets the {@link ShoppingItem} object's category
     * @param category The {@link ShoppingItem} object's category is set to this value
     */
    public void setCategory(String category) {
        this.setCategory(category);
    }

}
