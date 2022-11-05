package com.example.a301project;


import static org.junit.Assert.assertNotNull;

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
     * Tests that the IngredientFragment can open when the it's selected in the menu
     */
    @Test
    public void openIngredientFragment() {
        // Asserts that the current activity is the NavActivity. Otherwise show "Wrong Activity"
        solo.assertCurrentActivity("Not in NavActivity", NavActivity.class);

        // Click on button
        solo.clickOnMenuItem("Ingredients");

        // check if IngredientFragment opens
        assertNotNull(rule.getActivity().getSupportFragmentManager().findFragmentByTag("IngredientFragment"));
    }

    /**
     * Tests that the RecipeFragment can open when the it's selected in the menu
     */
    @Test
    public void openRecipeFragment() {
        // Asserts that the current activity is the NavActivity. Otherwise show "Wrong Activity"
        solo.assertCurrentActivity("Not in NavActivity", NavActivity.class);

        // Click on button
        solo.clickOnMenuItem("Recipes");

        // check if RecipeFragment opens
        solo.waitForText("My Recipes");
        assertNotNull(rule.getActivity().getSupportFragmentManager().findFragmentByTag("RecipeFragment"));
    }

    /**
     * Tests that the MealPlanFragment can open when the it's selected in the menu
     */
    @Test
    public void openMealPlanFragment() {
        // Asserts that the current activity is the NavActivity. Otherwise show "Wrong Activity"
        solo.assertCurrentActivity("Not in NavActivity", NavActivity.class);

        // Click on button
        solo.clickOnMenuItem("Meal Plan");

        // check if MealPlanFragment opens
        solo.waitForText("My Meal Plans");
        assertNotNull(rule.getActivity().getSupportFragmentManager().findFragmentByTag("MealPlanFragment"));
    }

    /**
     * Tests that the ShoppingListFragment can open when the it's selected in the menu
     */
    @Test
    public void openShoppingListFragment() {
        // Asserts that the current activity is the NavActivity. Otherwise show "Wrong Activity"
        solo.assertCurrentActivity("Not in NavActivity", NavActivity.class);

        // Click on button
        solo.clickOnMenuItem("Shopping List");

        // check if ShoppingListFragment opens
        solo.waitForText("My Shopping List");
        assertNotNull(rule.getActivity().getSupportFragmentManager().findFragmentByTag("ShoppingListFragment"));

    }

}
