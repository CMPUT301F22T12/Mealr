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
        Ingredient ing1 = new Ingredient("Carrot", 5.0, "2022-12-01", "Cupboard", "lbs", "Vegetables");
        Ingredient ing2 = new Ingredient("Cucumber", 2.5, "2022-12-02", "Fridge", "kg", "Vegetables");
        Ingredient ing3 = new Ingredient("Pineapple", 1.5, "2022-12-15", "Fridge", "lbs", "Fruits");
        ArrayList<Ingredient> ingredients1 = new ArrayList<>();
        ArrayList<Ingredient> ingredients2 = new ArrayList<>();
        ingredients1.add(ing1);
        ingredients1.add(ing2);
        ingredients2.add(ing3);

        Recipe rec1 = new Recipe("Salad", "Health Food", "Make sure ingredients are fresh!", null, 5L, 2L, ingredients2);
        ArrayList<Recipe> recipes1 = new ArrayList<>();
        recipes1.add(rec1);

        return new MealPlan(ingredients1, recipes1, "Healthy Meal Plan", "2022-11-28", "2022-12-05");
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
    public void testAddRecipe() {
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
}
