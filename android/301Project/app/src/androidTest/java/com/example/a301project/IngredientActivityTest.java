package com.example.a301project;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
     * Check that the IngredientActivity opens
     */
    @Test
    public void openIngredientActivity() {
        // Asserts that the current activity is the IngredientActivity.
        solo.assertCurrentActivity("Not in IngredientActivity", IngredientActivity.class);
    }

    /**
     * Test that the ADD ingredient button works
     * Steps:
     * 1. Check that IngredientActivity is open
     * 2. Click on Add button
     * 3. Check that the add ingredient popup is showing
     */
    @Test
    public void testAddButtonFunctionality() {
        // Asserts that the current activity is the IngredientActivity.
        solo.assertCurrentActivity("Not in IngredientActivity", IngredientActivity.class);

        // Click on button
        solo.clickOnButton("Add");

        // check if framework opens
        assertTrue("Add/Edit popup did not display after clicking add button", solo.waitForText("Add/Edit Entry", 1, 2000));
    }


    /**
     * Tests that the Ascending switch can be toggled
     * Steps:
     * 1. Check that IngredientActivity is open
     * 2. Ensure that the Ascending switch starting state is CHECKED
     * 3. Toggle the Ascending switch
     * 4. Check that Ascending switch starting state is NOT CHECKED
     * 5. Toggle the Ascending switch
     * 6. Ensure that the Ascending switch starting state is CHECKED
      */
    @Test
    public void testTogglingAscendingSwitch() {

        // Asserts that the current activity is the IngredientActivity.
        solo.assertCurrentActivity("Not in IngredientActivity", IngredientActivity.class);

        // get the switch
        Switch ascendingSwitch = (Switch) solo.getView(R.id.ingredientSortSwitch);

        // toggle the switch
        // need to implement this*

        // check that Switch is on
        assertTrue(ascendingSwitch.isChecked());
    }

    /**
     * Check that the cancel button closes the fragment
     * Steps:
     * 1. Open the fragment (using previous test)
     * 2. Click on Cancel button
     * 3. Check that the fragment is closed (add ingredient popup)
     *
     */
    @Test
    public void testCancelButtonFunctionality() {
        // Open the fragment
        testAddButtonFunctionality();

        // click on cancel button
        solo.clickOnButton("Cancel");

        // check that framework closed
        assertFalse("Add/Edit Framework did not close on Cancel Button", solo.waitForText("Add/Edit Entry", 1, 2000));
    }

    /**
     * Checks that all entry fields can be filled in the add ingredient popup framework
     * Steps:
     * 1. Open the add Ingredient popup fragment
     * 2. Enter correct info into all fields
     * 3. Check that each entry is correctly filled out
     */
    @Test
    public void testFillAllAddIngredientEntryFields() {
        // open the fragment
        testAddButtonFunctionality();

        // enter name
        solo.enterText((EditText) solo.getView(R.id.edit_name), "Carrot");
        assertTrue("Name not able to be entered properly in add/edit popup",
                solo.waitForText("Carrot", 1, 2000));

        // enter amount
        solo.clearEditText((EditText) solo.getView(R.id.edit_amount));
        solo.enterText((EditText) solo.getView(R.id.edit_amount), "5");
        assertTrue("Amount not able to be entered properly in add/edit popup",
                solo.waitForText("5", 1, 2000));

        // enter bestbeforedate
        solo.enterText((EditText) solo.getView(R.id.edit_bbd), "2022-08-11");
        assertTrue("BBD not able to be entered properly in add/edit popup",
                solo.waitForText("2022-08-11", 1, 2000));

        // enter location
        solo.enterText((EditText) solo.getView(R.id.edit_location), "Fridge");
        assertTrue("Location not able to be entered properly in add/edit popup",
                solo.waitForText("Fridge", 1, 2000));

        // enter unit
        solo.enterText((EditText) solo.getView(R.id.edit_unit), "lbs");
        assertTrue("Unit not able to be entered properly in add/edit popup",
                solo.waitForText("lbs", 1, 2000));

        // enter category
        solo.enterText((EditText) solo.getView(R.id.edit_category), "Vegetable");
        assertTrue("Category not able to be entered properly in add/edit popup",
                solo.waitForText("Vegetable", 1, 2000));
    }


    /**
     * Fill all entry fields in the Add Ingredient popup + check the functionality of the Confirm Button
     * Steps:
     * 1. Fill out all fields (using the previous test)
     * 2. Click on Confirm Button
     * 3. Check that the Add Ingredient popup framework closed and that the Ingredient Activity is open
     */
    @Test
    public void testAddIngredientConfirmButtonFunctionality() {
        // fill fields
        testFillAllAddIngredientEntryFields();

        // click confirm button
        solo.clickOnButton("Confirm");

        // check that fragment closed and the IngredientActivity is displayed
        assertFalse("Add/Edit Fragment not closed",solo.waitForText("Add/Edit Entry", 1, 2000));
        solo.assertCurrentActivity("IngredientActivity not displayed", IngredientActivity.class);
    }

    /**
     * Open fragment, fill out fields, and confirm, checks that ingredient was added
     * Steps:
     * 1. Open fragment and fill out fields and click confirm button (uses previous test)
     * 2. Check that the ingredient info is displayed in IngredientActivity
     */
    @Test
    public void testAddingIngredient() {
        // add ingredient
        testAddIngredientConfirmButtonFunctionality();

        // check that it was added
        assertTrue("Name: {Carrot} not found in IngredientActivity",
                solo.waitForText("Carrot", 1, 2000));
        assertTrue("Amount: {5} not found in IngredientActivity",
                solo.waitForText("5", 1, 2000));
        assertTrue("BBD: {2022-08-11} not found in IngredientActivity",
                solo.waitForText("2022-08-11", 1, 2000));
        assertTrue("Unit: {lbs} not found in IngredientActivity",
                solo.waitForText("lbs", 1, 2000));
        assertTrue("Location: {Fridge} not found in IngredientActivity",
                solo.waitForText("Fridge", 1, 2000));
        assertTrue("Category: {Vegetable} not found in IngredientActivity",
                solo.waitForText("Vegetable", 1, 2000));
    }

    // some common methods to add entry fields in the add/edit popup
    private void enterName() {
        // enter name
        solo.enterText((EditText) solo.getView(R.id.edit_name), "Carrot");
        assertTrue("Name not able to be entered properly in add/edit popup",
                solo.waitForText("Carrot", 1, 2000));
    }

    private void enterAmount() {
        // enter amount
        solo.clearEditText((EditText) solo.getView(R.id.edit_amount));
        solo.enterText((EditText) solo.getView(R.id.edit_amount), "5");
        assertTrue("Amount not able to be entered properly in add/edit popup",
                solo.waitForText("5", 1, 2000));
    }

    private void enterBBD() {
        // enter bestbeforedate
        solo.enterText((EditText) solo.getView(R.id.edit_bbd), "2022-08-11");
        assertTrue("BBD not able to be entered properly in add/edit popup",
                solo.waitForText("2022-08-11", 1, 2000));

    }

    private void enterLocation() {
        // enter location
        solo.enterText((EditText) solo.getView(R.id.edit_location), "Fridge");
        assertTrue("Location not able to be entered properly in add/edit popup",
                solo.waitForText("Fridge", 1, 2000));

    }

    private void enterUnit() {
        // enter unit
        solo.enterText((EditText) solo.getView(R.id.edit_unit), "lbs");
        assertTrue("Unit not able to be entered properly in add/edit popup",
                solo.waitForText("lbs", 1, 2000));
    }

    private void enterCategory() {
        // enter category
        solo.enterText((EditText) solo.getView(R.id.edit_category), "Vegetable");
        assertTrue("Category not able to be entered properly in add/edit popup",
                solo.waitForText("Vegetable", 1, 2000));
    }

    /**
     * Test that the error message is shown when trying to add ingredient with empty name
     * 1. Open the add/edit popup fragment
     * 2. Clear the name edittext
     * 3. Enter valid data in all other fields
     * 4. Click confirm button
     * 5. Check that the add/edit popup is still open
     */
    @Test
    public void testAddingIngredientWithEmptyName() {
        // open the fragment
        testAddButtonFunctionality();

        // clear name field
        solo.clearEditText((EditText) solo.getView(R.id.edit_name));

        // enter values
        enterAmount();
        enterBBD();
        enterLocation();
        enterUnit();
        enterCategory();

        // click confirm button
        solo.clickOnButton("Confirm");

        // check that error message is shown (or that fragment didn't close)
        assertTrue("Error when trying to add ingredient with empty name", solo.waitForText("Add/Edit Entry", 1, 2000));
    }

    /**
     * Test that the error message is shown when trying to add ingredient with empty amount
     * 1. Open the add/edit popup fragment
     * 2. Clear the amount edittext
     * 3. Enter valid data in all other fields
     * 4. Click confirm button
     * 5. Check that the add/edit popup is still open
     */
    @Test
    public void testAddingIngredientWithEmptyAmount() {
        // open the fragment
        testAddButtonFunctionality();

        // clear amount field
        solo.clearEditText((EditText) solo.getView(R.id.edit_amount));

        // enter values
        enterName();
        enterBBD();
        enterLocation();
        enterUnit();
        enterCategory();

        // click confirm button
        solo.clickOnButton("Confirm");

        // check that error message is shown (or that fragment didn't close)
        assertTrue("Error when trying to add ingredient with empty amount", solo.waitForText("Add/Edit Entry", 1, 2000));
    }

    /**
     * Test that the error message is shown when trying to add ingredient with empty bbd
     * 1. Open the add/edit popup fragment
     * 2. Clear the bbd edittext
     * 3. Enter valid data in all other fields
     * 4. Click confirm button
     * 5. Check that the add/edit popup is still open
     */
    @Test
    public void testAddingIngredientWithEmptyBBD() {
        // open the fragment
        testAddButtonFunctionality();

        // clear bbd field
        solo.clearEditText((EditText) solo.getView(R.id.edit_bbd));

        // enter values
        enterName();
        enterAmount();
        enterLocation();
        enterUnit();
        enterCategory();

        // click confirm button
        solo.clickOnButton("Confirm");

        // check that error message is shown (or that fragment didn't close)
        assertTrue("Error when trying to add ingredient with empty bbd", solo.waitForText("Add/Edit Entry", 1, 2000));
    }

    /**
     * Test that the error message is shown when trying to add ingredient with empty location
     * 1. Open the add/edit popup fragment
     * 2. Clear the location edittext
     * 3. Enter valid data in all other fields
     * 4. Click confirm button
     * 5. Check that the add/edit popup is still open
     */
    @Test
    public void testAddingIngredientWithEmptyLocation() {
        // open the fragment
        testAddButtonFunctionality();

        // clear name field
        solo.clearEditText((EditText) solo.getView(R.id.edit_location));

        // enter values
        enterName();
        enterAmount();
        enterBBD();
        enterUnit();
        enterCategory();

        // click confirm button
        solo.clickOnButton("Confirm");

        // check that error message is shown (or that fragment didn't close)
        assertTrue("Error when trying to add ingredient with empty location", solo.waitForText("Add/Edit Entry", 1, 2000));
    }

    /**
     * Test that the error message is shown when trying to add ingredient with empty unit
     * 1. Open the add/edit popup fragment
     * 2. Clear the unit edittext
     * 3. Enter valid data in all other fields
     * 4. Click confirm button
     * 5. Check that the add/edit popup is still open
     */
    @Test
    public void testAddingIngredientWithEmptyUnit() {
        // open the fragment
        testAddButtonFunctionality();

        // clear name field
        solo.clearEditText((EditText) solo.getView(R.id.edit_unit));

        // enter values
        enterName();
        enterAmount();
        enterBBD();
        enterLocation();
        enterCategory();

        // click confirm button
        solo.clickOnButton("Confirm");

        // check that error message is shown (or that fragment didn't close)
        assertTrue("Error when trying to add ingredient with empty unit", solo.waitForText("Add/Edit Entry", 1, 2000));
    }

    /**
     * Test that the error message is shown when trying to add ingredient with empty name
     * 1. Open the add/edit popup fragment
     * 2. Clear the category edittext
     * 3. Enter valid data in all other fields
     * 4. Click confirm button
     * 5. Check that the add/edit popup is still open
     */
    @Test
    public void testAddingIngredientWithEmptyCategory() {
        // open the fragment
        testAddButtonFunctionality();

        // clear name field
        solo.clearEditText((EditText) solo.getView(R.id.edit_category));

        // enter values
        enterName();
        enterAmount();
        enterBBD();
        enterLocation();
        enterUnit();

        // click confirm button
        solo.clickOnButton("Confirm");

        // check that error message is shown (or that fragment didn't close)
        assertTrue("Error when trying to add ingredient with empty category", solo.waitForText("Add/Edit Entry", 1, 2000));
    }










}
