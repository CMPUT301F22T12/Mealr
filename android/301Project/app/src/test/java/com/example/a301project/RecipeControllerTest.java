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
        return new Recipe("pizza", "fastfood", "food", "", 1L, 1L, new ArrayList<Ingredient>());
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
}
