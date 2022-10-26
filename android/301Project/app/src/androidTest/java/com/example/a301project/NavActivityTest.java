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
     * Tests that the IngredientActivity can open
     */
    @Test
    public void openIngredientActivity() {

        // Asserts that the current activity is the NavActivity. Otherwise show "Wrong Activity"
        solo.assertCurrentActivity("Not in NavActivity", NavActivity.class);

        // Click on button
        solo.clickOnButton(R.id.action_ingredients);

        // check if IngredientActivity opens
        assertTrue(solo.waitForText("My Ingredients"));
        solo.assertCurrentActivity("Did not open IngredientActivity", IngredientActivity.class);
    }


}
