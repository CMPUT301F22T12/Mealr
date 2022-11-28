package com.example.a301project;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Map;

public class RecipeControllerTest {
    private RecipeController controller;
    private CollectionReference mockCollectionRef;

    private Recipe mockRecipe() {
        return new Recipe("Pizza", "Fastfood", "Food", "", 1L, 1L, new ArrayList<Ingredient>());
    }

    @Before
    public void setUp() {
        // Add our mock classes
        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        mockCollectionRef = mock(CollectionReference.class, RETURNS_DEEP_STUBS);

        when(mockFirestore.collection(anyString()))
                .thenReturn(mockCollectionRef);

        this.controller = new RecipeController(mockFirestore);
    }

    @Test
    public void testAddRecipe() {
        Recipe i = mockRecipe();
        controller.addRecipe(i);
        ArgumentCaptor<Map<String, Object>> dataCaptor = ArgumentCaptor.forClass(Map.class);

        verify(mockCollectionRef)
                .add(dataCaptor.capture());
        Map<String, Object> data = dataCaptor.getValue();

        assertEquals(data.get("Title"), i.getTitle());
        assertEquals(data.get("Category"), i.getCategory());
        assertEquals(data.get("Comments"), i.getComments());
        assertEquals(data.get("Photo"), i.getPhoto());
        assertEquals(data.get("PrepTime"), i.getPrepTime());
        assertEquals(data.get("Servings"), i.getServings());
        assertEquals(data.get("Ingredients"), i.getIngredients());
    }

    @Test
    public void testRemoveRecipe() {
        Recipe i = mockRecipe();
        i.setId("TEST_ID");

        // add, then remove ingredient
        controller.addRecipe(i);
        controller.removeRecipe(i);

        // verify delete was called with the correct ID
        assertEquals(i.getId(), "TEST_ID");
        verify(mockCollectionRef.document(i.getId())).delete();
    }

    @Test
    public void testNotifyUpdate() {
        Recipe i = mockRecipe();
        i.setId("TEST_ID");
        controller.addRecipe(i);
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        Recipe u = new Recipe("Pizza", "Health Food", "It got healthier", "", 1L, 1L, ingredients);
        u.setId("TEST_ID");

        controller.notifyUpdate(u);

        ArgumentCaptor<Map<String, Object>> dataCaptor = ArgumentCaptor.forClass(Map.class);

        verify(mockCollectionRef.document(i.getId())).update(dataCaptor.capture());

        Map<String, Object> updatedRecipe = dataCaptor.getValue();

        // check to see if all values have been updated
        assertEquals("TEST_ID", u.getId());
        assertEquals("Pizza", updatedRecipe.get("Title"));
        assertEquals("Health Food", updatedRecipe.get("Category"));
        assertEquals("It got healthier", updatedRecipe.get("Comments"));
        assertEquals(ingredients, updatedRecipe.get("Ingredients"));
        assertEquals("", updatedRecipe.get("Photo"));
        assertEquals(1L, updatedRecipe.get("Servings"));
        assertEquals(1L, updatedRecipe.get("PrepTime"));
    }
}
