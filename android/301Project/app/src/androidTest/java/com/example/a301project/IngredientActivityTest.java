package com.example.a301project;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Switch;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test class for IngredientActivity. Robotium framework is used
 */
@RunWith(AndroidJUnit4.class)
public class IngredientActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<IngredientActivity> rule = new ActivityTestRule<>(IngredientActivity.class,
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
     * Check that the IngredientActivity opens
     */
    @Test
    public void openIngredientActivity() {
        // Asserts that the current activity is the IngredientActivity.
        solo.assertCurrentActivity("Not in IngredientActivity", IngredientActivity.class);
    }

    /**
     * Test that the ADD ingredient button works (the fragment opens)
     */
    @Test
    public void addIngredientButton() {
        // Asserts that the current activity is the IngredientActivity.
        solo.assertCurrentActivity("Not in IngredientActivity", IngredientActivity.class);

        // Click on button
        solo.clickOnButton("Add");

        // check if framework opens
        assertTrue(solo.waitForText("Add/Edit Entry", 1, 2000));
    }

    /**
     * Checks that the name of the ingredient list columns are diplayed
     */
    @Test
    public void checkIngredientListColumnTitles() {
        assertTrue(solo.waitForText("Entry", 1, 2000));
        assertTrue(solo.waitForText("#", 1, 2000));
        assertTrue(solo.waitForText("Unit", 1, 2000));
        assertTrue(solo.waitForText("Expiry", 1, 2000));
        assertTrue(solo.waitForText("Location", 1, 2000));
        assertTrue(solo.waitForText("Category", 1, 2000));
    }

    /**
     * Tests that the Ascending switch can be toggled
      */
    @Test
    public void testSwitchAscending() {
        // Asserts that the current activity is the IngredientActivity.
        solo.assertCurrentActivity("Not in IngredientActivity", IngredientActivity.class);

        // get the switch
        Switch ascendingSwitch = (Switch) solo.getView(R.id.ingredientSortSwitch);

        // check that Switch is on
        assertTrue(ascendingSwitch.isChecked());
    }

    /**
     * Check that the cancel button closes the fragment
     */
    @Test
    public void addIngredientCancelButton() {
        // Open the fragment
        addIngredientButton();

        // click on cancel button
        solo.clickOnButton("Cancel");

        // check that framework closed
        assertFalse(solo.waitForText("Add/Edit Entry", 1, 2000));

    }

    /**
     * Test that the confirm buttons works (on empty ingredient)
     */
    @Test
    public void addIngredientConfirmButton() {
        // Open the fragment
        addIngredientButton();

        // click on confirm button
        solo.clickOnButton("Confirm");
    }


    /**
     * Checks that all entry fields can be filled
     */
    @Test
    public void fillEntryFields() {
        // open the fragment
        addIngredientButton();

        // enter name
        solo.enterText((EditText) solo.getView(R.id.edit_name), "Carrot");
        assertTrue(solo.waitForText("Carrot", 1, 2000));

        // enter amount
        solo.clearEditText((EditText) solo.getView(R.id.edit_amount));
        solo.enterText((EditText) solo.getView(R.id.edit_amount), "5");
        assertTrue(solo.waitForText("5", 1, 2000));

        // enter bestbeforedate
        solo.enterText((EditText) solo.getView(R.id.edit_bbd), "23-08-2022");
        assertTrue(solo.waitForText("23-08-2022", 1, 2000));

        // enter location
        solo.enterText((EditText) solo.getView(R.id.edit_location), "Fridge");
        assertTrue(solo.waitForText("Fridge", 1, 2000));

        // enter unit
        solo.enterText((EditText) solo.getView(R.id.edit_unit), "lbs");
        assertTrue(solo.waitForText("lbs", 1, 2000));

        // enter category
        solo.enterText((EditText) solo.getView(R.id.edit_category), "Vegetable");
        assertTrue(solo.waitForText("Vegetable", 1, 2000));
    }


    /**
     * Fill the entry fields + check that confirmButton works
     */
    @Test
    public void confirmButton() {
        // fill fields
        fillEntryFields();

        // click confirm button
        solo.clickOnButton("Confirm");

        // check that fragment closes
        assertFalse(solo.waitForText("Add/Edit Entry", 1, 2000));
    }

    /**
     * Open fragment, fill out fields, and confirm, checks that ingredient was added
     */
    @Test
    public void addIngredient() {
        // add ingredient
        confirmButton();

        // check that it was added
        assertTrue(solo.waitForText("Carrot", 1, 2000));
        assertTrue(solo.waitForText("5", 1, 2000));
        assertTrue(solo.waitForText("lbs", 1, 2000));
        assertTrue(solo.waitForText("23-08-2022", 1, 2000));
        assertTrue(solo.waitForText("Fridge", 1, 2000));
        assertTrue(solo.waitForText("Vegetable", 1, 2000));
    }

    /**
     * Add a second ingredient and ensure that both are displayed
     */
    @Test
    public void addSecondIngredient() {
        // Add first ingredient
        addIngredient();
    }

    /**
     * Click on ingredient and check that edit fragment is shown
     */
    @Test
    public void clickOnIngredient() {

    }

    /**
     * Click on ingredient and verify that all entries in the edit fragment are shown
     */
    @Test
    public void viewIngredientDetails() {

    }

    /**
     * Test that the error message is shown when trying to add ingredient with empty name
     */
    @Test
    public void addIngredientWithMissingName() {

    }









}
