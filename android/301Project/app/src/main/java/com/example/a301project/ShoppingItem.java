package com.example.a301project;

public class ShoppingItem extends Ingredient{

    /**
     * Makes an {@link Ingredient} object from the given parameters
     *
     * @param name     The name of the object
     * @param amount   The amount of the object
     * @param unit     The unit the object uses
     * @param category The object's category
     */
    public ShoppingItem(String name, double amount, String unit, String category) {
        super(name, amount, null, null, unit, category);
    }
}
