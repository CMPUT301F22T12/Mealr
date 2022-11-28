package com.example.a301project;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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

@RunWith(AndroidJUnit4.class)
public class RecipeFragment {
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
     * Check that the RecipeFragment opens
     */
    @Test
    public void testThatRecipeFragmentIsOpen() {
        solo.waitForText("My Ingredients");

        solo.clickOnMenuItem("Recipes");
        solo.waitForText("My Recipes");

        assertNotNull(rule.getActivity().getSupportFragmentManager().findFragmentByTag("RecipeFragment"));
    }

    /**
     * Tests that the Name value is the default value in the SortBy {@link android.widget.Spinner}
     * in Ingredient Activity
     * Steps:
     * 1. Check that RecipeFragment is open
     * 2. Check that the Name value is the selected in the SortBy {@link android.widget.Spinner}
     */
    @Test
    public void testThatNameIsDefaultSortBySelection() {
        // check that RecipeFragment is open
        testThatRecipeFragmentIsOpen();

        // check that the item was selected
        assertTrue("{Name} was not the default selection for the Sort By spinner in RecipeFragmentCategory",
                solo.waitForText("Name", 1, 2000));
    }

    /**
     * Test that the ADD ingredient button works
     * Steps:
     * 1. Check that RecipeFragment is open
     * 2. Click on Add button
     * 3. Check that the add recipe popup is showing
     */
    @Test
    public void testAddButtonFunctionality() {
        testThatRecipeFragmentIsOpen();

        // Click on button
        solo.clickOnButton("Add");

        // check if framework opens
        assertTrue("Add Recipe fragment did not display after clicking add button", solo.waitForText("Add Recipe", 1, 2000));
    }

    /**
     * Tests that the Location value can be selected in the SortBy {@link android.widget.Spinner}
     * in Recipe Activity
     * Steps:
     * 1. Check that RecipeFragment is open
     * 2. Check that Name value is currently selected in the Sort By {@link android.widget.Spinner}
     * 3. Open the spinner and select the Location value
     * 4. Check that the Prep Time value is the selected in the Sort By {@link android.widget.Spinner}
     */
    @Test
    public void testSelectingLocationFromSortBySpinner() {
        // ensure that RecipeFragment is open and Name is the default value for the Sort By spinner
        testThatNameIsDefaultSortBySelection();

        // click on spinner and select the item
        solo.clickOnText("Name");
        solo.clickOnText("Prep time");

        // check that the item was selected
        assertTrue("{Prep time} value was not able to be selected from the Sort By Spinner in RecipeFragment",
                solo.waitForText("Prep time", 1, 2000));
    }

    /**
     * Fill all entry fields in the Add Recipe popup + check the functionality of the Confirm Button
     * Steps:
     * 1. Fill out all fields (using the previous test)
     * 2. Click on Confirm Button
     * 3. Check that the Add Ingredient popup framework closed and that the Ingredient Activity opened
     */
    @Test
    public void testAddRecipeFullFlow() {
        // Open the Add entry framework
        testAddButtonFunctionality();

        // fill fields
        solo.enterText((EditText) solo.getView(R.id.edit_prep_time), "TestRecipe");
        solo.enterText((EditText) solo.getView(R.id.edit_servings), "1");

        // click confirm button
        solo.clickOnButton("Confirm");

        // check that fragment closed and the RecipeFragment is displayed
        assertFalse("Add RecipeFragment not closed when pressed Confirm Button after filling out all fields correctly",
                solo.waitForText("Add Entry", 1, 2000));
    }
}

