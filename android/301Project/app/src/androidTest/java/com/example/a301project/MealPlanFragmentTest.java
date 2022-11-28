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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Test class for MealPlanFragment. Robotium framework is used
 */
@RunWith(AndroidJUnit4.class)
public class MealPlanFragmentTest {

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
        solo.clickOnMenuItem("Meal Plan");

        // check if RecipeFragment opens
        solo.waitForText("My Meal Plans");

        assertNotNull(rule.getActivity().getSupportFragmentManager().findFragmentByTag("MealPlanFragment"));
        solo.sleep(2000);
    }

    /**
     * Test that the ADD ingredient button works
     * Steps:
     * 1. Click on Add button
     * 3. Check that the AddEditMealPlanFragment is showing
     */
    @Test
    public void testAddButtonFunctionality() {

        // Click on button
        solo.clickOnButton("Add");

        // check if framework opens
        assertTrue("AddEditMealPlanfragment did not display after clicking Add button", solo.waitForText("Add Entry", 1, 2000));
    }


    /**
     * Check when the CANCEL button is pressed on the AddEditMealPlanFragment it goes
     * back to MealPlanFragment
     * Steps:
     * 1. Open the fragment (using previous test)
     * 2. Click on Cancel button
     * 3. Check that the Warning message is showing
     *
     */
    @Test
    public void testCancelButtonFunctionality() {
        // Open the fragment
        testAddButtonFunctionality();

        // click on cancel button
        solo.clickOnButton("Cancel");

        // wait for the warning message
        assertTrue("Did not go back to MealPlanFragmnet after clicking Cancel button on AddEditMealPlanFragment", solo.waitForText("My Meal Plans", 1, 2000));
    }


    /**
     * Tests that the {@link String} can be entered into the mealplan name field
     * in the AddEditMealPlanFragent
     * Steps:
     * 1. Open AddEditMealPlanFragment
     * 2. Clear the name field
     * 3. Enter a value {@link String} into the MealPlanName edittext
     * 4. Check that the value {@link String} was entered
     */
    @Test
    public void testFillingNameFieldInAddIngredientFragment() {
        // Open the Add Ingredient fragment
        testAddButtonFunctionality();

        // clear field + enter value + check that value was entered
        enterMealPlanName("Pasta");
    }

    /**
     * Tests that the start date in the AddEditMealPlan Fragment
     * Steps:
     * 1. Open AddEditMealPlanFragment
     * 2. Try to open the start date date-picker and select a value
     * 3. Check that the date was selected
     */
    @Test
    public void testSelectingStartDateInAddEditMealPlanFragment() {
        // Open the Add Ingredient fragment
        testAddButtonFunctionality();

        // select date + check value was selected
        selectDate("startdate");
    }

    /**
     * Tests that the date date in the AddEditMealPlan Fragment
     * Steps:
     * 1. Open AddEditMealPlanFragment
     * 2. Try to open the start date date-picker and select a value
     * 3. Check that the date was selected
     */
    @Test
    public void testSelectingEndDateInAddEditMealPlanFragment() {
        // Open the Add Ingredient fragment
        testAddButtonFunctionality();

        // select date + check value was selected
        selectDate("enddate");
    }

    // some common methods to add entry fields in the add/edit popup
    private void enterMealPlanName(String mealPlanName) {
        // enter name
        solo.enterText((EditText) solo.getView(R.id.edit_title_mealplan), mealPlanName);
        solo.sleep(100);
        assertTrue("Name not able to be entered properly in AddEditMealPlanFragment",
                solo.waitForText(mealPlanName, 1, 2000));
    }

    private void selectDate(String dateselection) {
        int matches = 1;
        int id = 0;
        if (dateselection.equalsIgnoreCase("startdate")) {
            id = R.id.edit_start_date;
        } else if (dateselection.equalsIgnoreCase("enddate")) {
            matches = 2;
            id = R.id.edit_end_date;
        }
        // enter bestbeforedate
        EditText editText = (EditText) solo.getView(id);
        solo.clickOnView(editText);
        solo.clickOnText("OK");
        solo.sleep(100);

        // get today's date
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(date);
        solo.sleep(100);

        // check that the date is showing correctly
        assertTrue("BBD not able to be entered properly in add/edit popup",
                solo.waitForText(formattedDate, 2, 2000));
        solo.sleep(100);
    }
}