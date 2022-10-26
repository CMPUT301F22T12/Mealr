package com.example.a301project;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class IngredientControllerTest {
    private IngredientController controller;

    private Ingredient mockIngredient() {
        return new Ingredient("carrot",3,"2022-11-30","pantry","lbs","vegetable");
    }

    @Before
    public void setUp() {
        this.controller = new IngredientController();
    }

    @Test
    public void testAddRemove() throws InterruptedException{
        Ingredient ingredient = mockIngredient();
        controller.addIngredient(ingredient);
        Thread.sleep(10000);
        controller.removeIngredient(ingredient);
    }
}
