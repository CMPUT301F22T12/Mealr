package com.example.a301project;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for ShoppingListFragment. Robotium framework is used
 */
@RunWith(AndroidJUnit4.class)
public class ShoppingListFragmentTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<NavActivity> rule = new ActivityTestRule<>(NavActivity.class,
            true, true);

    /**
     * RUns before all tests and creates solo instance
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        // open the ShoppingListFragment
        // Asserts that the current activity is the NavActivity. Otherwise show "Wrong Activity"
        solo.assertCurrentActivity("Not in NavActivity", NavActivity.class);

        // Click on button
        solo.clickOnMenuItem("Shopping List");

        // check if RecipeFragment opens
        solo.waitForText("Shopping List");

        assertNotNull(rule.getActivity().getSupportFragmentManager().findFragmentByTag("ShoppingListFragment"));
        solo.sleep(2000);
    }

    /**
     * Test that the purchase button works
     * Steps:
     * 1. Click on the first purchase button
     * 2. Check that the AddEditIngredientFragment is showing
     */
    @Test
    public void testClickingPurchaseButton() {

        // Click on button
        solo.clickOnButton("Purchase");

        // check if RecipeFragment opens
        solo.waitForText("Shopping List");

        assertTrue("AddEditIngredientFragment did not display after clicking purchase button",
                solo.waitForText("Best Before", 1, 2000));
    }

    /**
     * Tests that the Name value is the default value in the SortBy {@link android.widget.Spinner}
     * in ShoppingListFragment
     * Steps:
     * 1. Check that the Name value is the selected in the SortBy {@link android.widget.Spinner}
     */
    @Test
    public void testThatNameIsDefaultSortBySelection() {

        // check that the item was selected
        assertTrue("{Name} was not the default selection for the Sort By spinner in ShoppingListFragment",
                solo.waitForText("Name", 1, 2000));
    }

    /**
     * Tests that the Category value can be selected in the SortBy {@link android.widget.Spinner}
     * in ShoppingListFragment
     * Steps:
     * 1. Check that Name value is currently selected in the Sort By {@link android.widget.Spinner}
     * 2. Open the spinner and select the Category value
     * 3. Check that the Category value is the selected in the Sort By {@link android.widget.Spinner}
     */
    @Test
    public void testSelectingLocationFromSortBySpinner() {
        // ensure that Name is the default value for the Sort By spinner
        testThatNameIsDefaultSortBySelection();

        // click on spinner and select the item
        solo.clickOnText("Name");
        solo.sleep(200);
        solo.clickOnText("Category");

        // check that the item was selected
        assertTrue("{Category} value was not able to be selected from the Sort By Spinner in ShoppingListFragment",
                solo.waitForText("Category", 1, 2000));
    }

    /**
     * Check when the CANCEL button is pressed on the AddEditIngredientFragment
     * the Warning message pops up
     * Steps:
     * 1. Open the fragment (using previous test)
     * 2. Click on Cancel button
     * 3. Check that the Warning message is showing
     *
     */
    @Test
    public void testCancelButtonFunctionality() {
        // Open the fragment
        testClickingPurchaseButton();

        // click on cancel button
        solo.clickOnButton("Cancel");

        // wait for the warning message
        assertTrue("The Warning did not show after clicking Cancel button on AddEditIngredientFragment in ShoppingListFragment", solo.waitForText("WARNING", 1, 2000));
    }


    /**
     * Check when the CANCEL button is pressed in the Warning Dialog
     * Steps:
     * 1. Open the fragment (using previous test)
     * 2. Check that the Warning message is showing
     * 3. Click on OK button
     * 4. Check that it goes back to ShoppingListFragment
     *
     */
    @Test
    public void testWarningDialogCancelButtonFunctionality() {
        // Open fragment and click Cancel -> ensure Warning message
        testCancelButtonFunctionality();

        // click on cancel button
        solo.clickOnButton("Cancel");

        // wait for the warning message
        assertTrue("Clicking Cancel on the Warning Dialog did not go back to the AddEditIngredientFragment in ShoppingListFragment", solo.waitForText("Best Before", 1, 2000));
    }

    /**
     * Check when the OK button in the Warning dialog
     * Steps:
     * 1. Open the fragment (using previous test)
     * 2. Check that the Warning message is showing
     * 3. Click on OK button
     * 4. Check that it goes back to ShoppingListFragment
     *
     */
    @Test
    public void testWarningDialogOKButtonFunctionality() {
        // Open fragment and click Cancel -> ensure Warning message
        testCancelButtonFunctionality();

        // click on OK button
        solo.clickOnButton("OK");

        // wait for the warning message
        assertTrue("Clicking OK on the Warning Dialog did not go back to the ShoppingListFragment", solo.waitForText("Croutons", 1, 2000));
    }




}