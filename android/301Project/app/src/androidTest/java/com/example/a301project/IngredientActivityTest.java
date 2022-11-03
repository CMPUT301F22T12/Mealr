package com.example.a301project;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


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
    public void testThatIngredientActivityIsOpen() {
        // Asserts that the current activity is the IngredientActivity.
        solo.assertCurrentActivity("IngredientActivity did not open on startup", IngredientActivity.class);
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
        testThatIngredientActivityIsOpen();

        // Click on button
        solo.clickOnButton("Add");

        // check if framework opens
        assertTrue("Add Ingredient fragment did not display after clicking add button", solo.waitForText("Add Entry", 1, 2000));
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
    /*
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
    }*/

    /**
     * Tests that the Name value is the default value in the SortBy {@link android.widget.Spinner}
     * in Ingredient Activity
     * Steps:
     * 1. Check that IngredientActivity is open
     * 2. Check that the Name value is the selected in the SortBy {@link android.widget.Spinner}
     */
    @Test
    public void testThatNameIsDefaultSortBySelection() {
        // check that IngredientActivity is open
        testThatIngredientActivityIsOpen();

        // check that the item was selected
        assertTrue("{Name} was not the default selection for the Sort By spinner in IngredientActivityCategory",
                solo.waitForText("Name", 1, 2000));
    }

    /**
     * Tests that the Location value can be selected in the SortBy {@link android.widget.Spinner}
     * in Ingredient Activity
     * Steps:
     * 1. Check that IngredientActivity is open
     * 2. Check that Name value is currently selected in the Sort By {@link android.widget.Spinner}
     * 3. Open the spinner and select the Location value
     * 4. Check that the Location value is the selected in the Sort By {@link android.widget.Spinner}
     */
    @Test
    public void testSelectingLocationFromSortBySpinner() {
        // ensure that IngredientActivity is open and Name is the default value for the Sort By spinner
        testThatNameIsDefaultSortBySelection();

        // click on spinner and select the item
        solo.clickOnText("Name");
        solo.clickOnText("Location");

        // check that the item was selected
        assertTrue("{Location} value was not able to be selected from the Sort By Spinner in IngredientActivity",
                solo.waitForText("Location", 1, 2000));
    }

    /**
     * Tests that the Expiry value can be selected in the SortBy {@link android.widget.Spinner}
     * in Ingredient Activity
     * Steps:
     * 1. Check that IngredientActivity is open
     * 2. Check that Name value is currently selected in the Sort By spinner
     * 3. Open the spinner and select the Expiry value
     * 4. Check that the Expiry value is the selected in the Sort By  {@link android.widget.Spinner}
     */
    @Test
    public void testSelectingExpiryFromSortBySpinner() {
        // ensure that IngredientActivity is open and Name is the default value for the Sort By spinner
        testThatNameIsDefaultSortBySelection();

        // click on spinner and select the item
        solo.clickOnText("Name");
        solo.clickOnText("Expiry");

        // check that the item was selected
        assertTrue("{Expiry} value was not able to be selected from the Sort By Spinner in IngredientActivity",
                solo.waitForText("Expiry", 1, 2000));

    }

    /**
     * Tests that the Category value can be selected in the SortBy {@link android.widget.Spinner}
     * in Ingredient Activity
     * Steps:
     * 1. Check that IngredientActivity is open
     * 2. Check that Name value is currently selected in the Sort By spinner
     * 3. Open the spinner and select the Category value
     * 4. Check that the Category value is the selected in the Sort By  {@link android.widget.Spinner}
     */
    @Test
    public void testSelectingCategoryFromSortBySpinner() {
        // ensure that IngredientActivity is open and Name is the default value for the Sort By spinner
        testThatNameIsDefaultSortBySelection();

        // click on spinner and select the item
        solo.clickOnText("Name");
        solo.clickOnText("Category");

        // check that the item was selected
        assertTrue("{Category} value was not able to be selected from the Sort By Spinner in IngredientActivity",
                solo.waitForText("Category", 1, 2000));

    }

    /**
     * Check that the cancel button closes the Add Ingredient fragment
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
        assertFalse("Add Ingredient fragment did not close on Cancel Button", solo.waitForText("Add Entry", 1, 2000));
    }

    /**
     * Tests that the {@link String} can be entered into the ingredient name field
     * in the Add Ingredient fragment
     * Steps:
     * 1. Open Add Ingredient fragment
     * 2. Clear the name field
     * 3. Enter a value {@link String} into the ingredient name edittext
     * 4. Check that the value {@link String} was entered
     */
    @Test
    public void testFillingNameFieldInAddIngredientFragment() {
        // Open the Add Ingredient fragment
        testAddButtonFunctionality();

        // clear field + enter value + check that value was entered
        enterIngredientName("Carrot");
    }

    /**
     * Tests that the an {@link Double} can be entered into the amount field
     * in the Add Ingredient fragment
     * Steps:
     * 1. Open Add Ingredient fragment
     * 2. Clear the amount field
     * 3. Enter a value {@link Double} into the ingredient amount edittext
     * 4. Check that the value {@link Double} was entered
     */
    @Test
    public void testFillingAmountFieldInAddIngredientFragment() {
        // Open the Add Ingredient fragment
        testAddButtonFunctionality();

        // clear field + enter value + check that value was entered
        enterAmount(2.5);
    }

    /**
     * Tests that the category can be selected from the spinner
     * in the Add Ingredient fragment
     * Steps:
     * 1. Open Add Ingredient fragment
     * 2. Try to open Spinner and select a different value
     * 3. Check that the value was selected
     */
    @Test
    public void testSelectingCategoryInAddIngredientFragment() {
        // Open the Add Ingredient fragment
        testAddButtonFunctionality();

        // select location + check that the value was selected
        selectCategory("Grain");
    }

    /**
     * Tests that the best before date can be selected from the date picker
     * in the Add Ingredient fragment
     * Steps1:
     * 1. Open Add Ingredient fragment
     * 2. Try to open the datepicker and select a value
     * 3. Check that the date was selected
     */
    @Test
    public void testSelectingBestBeforeDateInAddIngredientFragment() {
        // Open the Add Ingredient fragment
        testAddButtonFunctionality();

        // select date + check value was selected
        selectBestBeforeDate();
    }

    /**
     * Tests that the location can be selected from the spinner
     * in the Add Ingredient fragment
     * Steps:
     * 1. Open Add Ingredient fragment
     * 2. Try to open Spinner and select a different value
     * 3. Check that the value was selected
     */
    @Test
    public void testSelectingLocationInAddIngredientFragment() {
        // Open the Add Ingredient fragment
        testAddButtonFunctionality();

        // select location + check that the value was selected
        selectLocation("Cabinet");
    }

    /**
     * Tests that the unit can be selected from the spinner
     * in the Add Ingredient fragment
     * Steps:
     * 1. Open Add Ingredient fragment
     * 2. Try to open the unit Spinner and selecting a value
     * 3. Check that the value was selected
     */
    @Test
    public void testSelectingUnitInAddIngredientFragment() {
        // Open the Add Ingredient fragment
        testAddButtonFunctionality();

       // select unit + check that value was selected
        selectUnit("Slices");
    }



    /**
     * Fill all entry fields in the Add Ingredient popup + check the functionality of the Confirm Button
     * Steps:
     * 1. Fill out all fields (using the previous test)
     * 2. Click on Confirm Button
     * 3. Check that the Add Ingredient popup framework closed and that the Ingredient Activity opened
     */
    @Test
    public void testAddIngredientFrameworkConfirmButton() {
        // Open the Add entry framework
        testAddButtonFunctionality();

        // fill fields
        enterIngredientName("Pineapple");
        enterAmount(3.5);
        selectCategory("Grain");
        selectBestBeforeDate();
        selectLocation("Cabinet");
        selectUnit("Slices");

        // click confirm button
        solo.clickOnButton("Confirm");

        // check that fragment closed and the IngredientActivity is displayed
        assertFalse("Add Ingredient fragment not closed when pressed Confirm Button after filling out all fields correctly",
                solo.waitForText("Add Entry", 1, 2000));
        solo.assertCurrentActivity("IngredientActivity not displayed after adding ingredient", IngredientActivity.class);
    }

    /**
     * Open fragment, fill out fields, and confirm, checks that ingredient was added
     * Steps:
     * 1. Open fragment and fill out fields and click confirm button (uses previous test)
     * 2. Check that the ingredient info is displayed in IngredientActivity
     */
    @Test
    public void testAddingCorrectIngredient() {
        // add ingredient
        //testAddIngredientFrameworkConfirmButton();

        // scroll down until the name of the ingredient is found
        assertTrue("Added an Ingredient and it cannot be found in Ingredient Activity",
                scrollIngredientActivityUntilIngredientIsFound("Pineapple", "3.5", "Fruit",
                        "2022-11-03", "Cabinet", "Pieces"));
    }

    // some common methods to add entry fields in the add/edit popup
    private void enterIngredientName(String ingredientName) {
        // enter name
        solo.enterText((EditText) solo.getView(R.id.edit_name), ingredientName);
        assertTrue("Name not able to be entered properly in Add Ingredient fragment",
                solo.waitForText(ingredientName, 1, 2000));
    }

    private void enterAmount(Double amount) {
        // enter amount
        solo.clearEditText((EditText) solo.getView(R.id.edit_amount));
        solo.enterText((EditText) solo.getView(R.id.edit_amount), String.valueOf(amount));
        assertTrue("Amount not able to be entered properly in Add Ingredient fragment",
                solo.waitForText(String.valueOf(amount), 1, 2000));
    }

    private void selectCategory(String item) {
        // click on spinner and select the item
        solo.clickOnText("Fruit");
        solo.clickOnText(item);

        // check that the item was selected
        assertTrue("{" + item + "} value was not able to be selected for Category Spinner on Add Ingredient fragment",
                solo.waitForText(item, 1, 2000));
    }

    private void selectBestBeforeDate() {
        // enter bestbeforedate
        EditText editText = (EditText) solo.getView(R.id.edit_bbd);
        solo.clickOnView(editText);
        solo.clickOnText("OK");

        // get today's date
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(date);

        // check that the date is showing correctly
        assertTrue("BBD not able to be entered properly in add/edit popup",
                solo.waitForText(formattedDate, 1, 2000));

    }

    private void selectLocation(String item) {
        // click on spinner and select the item
        solo.clickOnText("Fridge");
        solo.clickOnText(item);

        // check that the item was selected
        assertTrue("{" + item + "} value was not able to be selected for Location Spinner on Add Ingredient fragment",
                solo.waitForText(item, 1, 2000));
    }

    private void selectUnit(String item) {
        // click on spinner and select the item
        solo.clickOnText("Pieces");
        solo.clickOnText(item);

        // check that the item was selected
        assertTrue("{" + item + "} value was not able to be selected for Unit Spinner on Add Ingredient fragment",
                solo.waitForText(item, 1, 2000));
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
        enterAmount(2.5);
        selectCategory("Grain");
        selectBestBeforeDate();
        selectLocation("Cabinet");
        selectUnit("Slices");

        // click confirm button
        solo.clickOnButton("Confirm");


        // check that error message is shown (or that fragment didn't close)
        assertTrue("Error in Add fragment when trying to Add Ingredient with empty name",
                solo.waitForText("Rejected: Missing Field(s)", 1, 2000));

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
        enterIngredientName("Carrot");
        selectCategory("Grain");
        selectBestBeforeDate();
        selectLocation("Cabinet");
        selectUnit("Slices");

        // click confirm button
        solo.clickOnButton("Confirm");

        // check that error message is shown (or that fragment didn't close)

        assertTrue("Error in Add fragment when trying to Add Ingredient with empty amount",
                solo.waitForText("Rejected: Missing Field(s)", 1, 2000));
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


        // enter values
        enterIngredientName("Carrot");
        enterAmount(2.5);
        selectCategory("Grain");
        selectLocation("Cabinet");
        selectUnit("Slices");

        // click confirm button
        solo.clickOnButton("Confirm");

        // check that error message is shown (or that fragment didn't close)
        assertFalse("Error in Add fragment when trying to Add Ingredient with empty bbd", solo.waitForText("Add Entry", 1, 2000));
    }

    /**
     * Scrolls down until a certain text value is found
     * @param text The {@link String} to find when scrolling
     * @return if the text {@link String} was found while scrolling
     */
    private boolean scrollIngredientsUntilIngredientIsFound(String text) {

        // start off at the top
        boolean scrollMore = true;
        solo.scrollToTop();

        // keep scrolling until value is found or until can't scroll anymore
        while(scrollMore) {
            scrollMore = solo.scrollDown();
            if (solo.waitForText(text, 1, 100)) {
                return true;
            }
        }
        // text was not found
        return false;
    }

    /**
     * Scrolls down Ingredient Activity until an Ingredient is found
     * @param name the name {@link String} of the ingredient {@link Ingredient}
     * @param amount the amount {@link String} of the ingredient {@link Ingredient}
     * @param category the category {@link String} of the ingredient {@link Ingredient}
     * @param bbd the best before date {@link String} of the ingredient {@link Ingredient}
     * @param location the location {@link String} of the ingredient {@link Ingredient}
     * @param unit the unit {@link String} of the ingredient {@link Ingredient}
     * @return
     */
    public boolean scrollIngredientActivityUntilIngredientIsFound(String name, String amount, String category,
                                                                  String bbd, String location, String unit){
        // start off at the top
        boolean scrollMore = true;
        solo.scrollToTop();

        // keep scrolling until Ingredient is found or until can't scroll anymore
        while(scrollMore) {
            scrollMore = solo.scrollDown();
            if(solo.waitForText(name, 1, 100) &
                    solo.waitForText(amount, 1, 100) &
                    solo.waitForText(category, 1, 100) &
                    solo.waitForText("Expires: " + bbd, 1, 100) &
                    solo.waitForText(location, 1, 100) &
                    solo.waitForText(unit, 1, 100)){
                // found ingredient
                return true;
            }
        }
        return false;
    }

    /**
     * Taps on an ingredients and see whether the edit fragment pops up
     */
    @Test
    public void testSelectingIngredient() {
        // add an ingredient and scroll down to it
        testAddingCorrectIngredient();
    }




    /**
     * Try to add letters into the amount edittext in the add/edit IngredientFragment
     * and ensure that they weren't allowed
     * Steps:
     * 1. Open the add/edit Ingredient popup fragment
     * 2. Clear the amount edit text
     * 3. Try to enter some letters
     * 4. Check that the letters weren't entered
     */
/*
@Test
public void testLettersInAmountField() {
    // open the add/edit Ingredient popup fragment
    testAddButtonFunctionality();

    // clear amount field
    solo.clearEditText((EditText) solo.getView(R.id.edit_amount));

    // Try to add letters to the amount field
    solo.enterText((EditText) solo.getView(R.id.edit_amount), "abcde");

    // check that the letters weren't entered
    assertFalse("Letters were able to be entered into amount field for the Add Ingredient fragment",
            solo.waitForText("abcde", 1, 2000));
}*/












}
