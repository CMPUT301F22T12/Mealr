package com.example.a301project;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Map;

public class MealPlanControllerTest {
    private MealPlanController controller;
    private CollectionReference mockCollectionRef;

    private MealPlan mockMealPlan() {
        return new MealPlan(new ArrayList<Ingredient>(), new ArrayList<Recipe>(), "Healthy Meal Plan", "2022-11-28", "2022-12-05");
    }

    @Before
    public void setUp() {
        // Add our mock classes
        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        mockCollectionRef = mock(CollectionReference.class, RETURNS_DEEP_STUBS);

        when(mockFirestore.collection(anyString()))
                .thenReturn(mockCollectionRef);

        this.controller = new MealPlanController(mockFirestore);
    }

    @Test
    public void testAddMealPlan() {
        MealPlan i = mockMealPlan();
        controller.addMealPlan(i);
        ArgumentCaptor<Map<String, Object>> dataCaptor = ArgumentCaptor.forClass(Map.class);

        verify(mockCollectionRef)
                .add(dataCaptor.capture());
        Map<String, Object> data = dataCaptor.getValue();

        assertEquals(data.get("Title"), i.getName());
        assertEquals(data.get("Start Date"), i.getStartDate());
        assertEquals(data.get("End Date"), i.getEndDate());
        assertEquals(data.get("Ingredients"), i.getIngredients());
        assertEquals(data.get("Recipes"), i.getRecipes());
    }

    @Test
    public void testRemoveMealPlan() {
        MealPlan i = mockMealPlan();
        i.setId("TEST_ID");

        // add, then remove ingredient
        controller.addMealPlan(i);
        controller.removeMealPlan(i);

        // verify delete was called with the correct ID
        assertEquals(i.getId(), "TEST_ID");
        verify(mockCollectionRef.document(i.getId())).delete();
    }

    @Test
    public void testNotifyUpdate() {
        MealPlan i = mockMealPlan();
        i.setId("TEST_ID");
        controller.addMealPlan(i);
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        MealPlan u = new MealPlan(new ArrayList<Ingredient>(), new ArrayList<Recipe>(), "Fun Meal Plan", "2022-11-28", "2022-12-20");
        u.setId("TEST_ID");

        controller.notifyUpdate(u);

        ArgumentCaptor<Map<String, Object>> dataCaptor = ArgumentCaptor.forClass(Map.class);

        verify(mockCollectionRef.document(i.getId())).update(dataCaptor.capture());

        Map<String, Object> updatedMealPlan = dataCaptor.getValue();

        // check to see if all values have been updated
        assertEquals("TEST_ID", u.getId());
        assertEquals("Fun Meal Plan", updatedMealPlan.get("Title"));
        assertEquals(ingredients, updatedMealPlan.get("Ingredients"));
        assertEquals(recipes, updatedMealPlan.get("Recipes"));
        assertEquals("2022-11-28", updatedMealPlan.get("Start Start"));
        assertEquals("2022-12-20", updatedMealPlan.get("End Start"));
    }
}
