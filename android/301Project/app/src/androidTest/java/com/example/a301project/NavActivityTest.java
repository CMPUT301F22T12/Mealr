package com.example.a301project;


import static org.junit.Assert.assertTrue;

import android.app.Activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for NavActivity. Robotium test framework is used
 */
@RunWith(AndroidJUnit4.class)
public class NavActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<NavActivity> rule = new ActivityTestRule<>(NavActivity.class,
            true, true);

    /**
     * RUns before all tests and creates solo instance
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Check that the NavActivity opens
     */
    @Test
    public void openNavActivity() {
        // Asserts that the current activity is the NavActivity. Otherwise show "Wrong Activity"
        solo.assertCurrentActivity("Not in NavActivity", NavActivity.class);
    }

    /**
     * Tests that the IngredientActivity can open when the it's selected in the menu
     */
    @Test
    public void openIngredientActivity() {
        // Asserts that the current activity is the NavActivity. Otherwise show "Wrong Activity"
        solo.assertCurrentActivity("Not in NavActivity", NavActivity.class);

        // Click on button
        solo.clickOnMenuItem("Ingredients");

        // check if IngredientActivity opens
        assertTrue(solo.waitForText("My Ingredients", 1, 2000));
        solo.assertCurrentActivity("Did not open IngredientActivity", IngredientActivity.class);
    }

    /**
     * Tests that the RecipeActivity can open when the it's selected in the menu
     */
    @Test
    public void openRecipeActivity() {
        // Asserts that the current activity is the NavActivity. Otherwise show "Wrong Activity"
        solo.assertCurrentActivity("Not in NavActivity", NavActivity.class);

        // Click on button
        solo.clickOnMenuItem("Recipes");

        // check if IngredientActivity opens
        assertTrue(solo.waitForText("My Recipes", 1, 2000));
        solo.assertCurrentActivity("Did not open RecipeActivity", RecipeActivity.class);
    }

    /**
     * Tests that the MealPlanActivity can open when the it's selected in the menu
     */
    @Test
    public void openMealPlanActivity() {
        // Asserts that the current activity is the NavActivity. Otherwise show "Wrong Activity"
        solo.assertCurrentActivity("Not in NavActivity", NavActivity.class);

        // Click on button
        solo.clickOnMenuItem("Meal Plan");

        // check if IngredientActivity opens
        assertTrue(solo.waitForText("My Meal Plans", 1, 2000));
        solo.assertCurrentActivity("Did not open MealPlanActivity", MealPlanActivity.class);
    }

    /**
     * Tests that the ShoppingListActivity can open when the it's selected in the menu
     */
    @Test
    public void openShoppingListActivity() {
        // Asserts that the current activity is the NavActivity. Otherwise show "Wrong Activity"
        solo.assertCurrentActivity("Not in NavActivity", NavActivity.class);

        // Click on button
        solo.clickOnMenuItem("Shopping List");

        // check if IngredientActivity opens
        assertTrue(solo.waitForText("My Shopping List", 1, 2000));
        solo.assertCurrentActivity("Did not open ShoppingListActivity", ShoppingListActivity.class);

    }

}
