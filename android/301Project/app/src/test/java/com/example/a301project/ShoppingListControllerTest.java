package com.example.a301project;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.anyString;
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


public class ShoppingListControllerTest {
    'private ShoppingListController controller;'
    private RecipeController RecipeController;
    private FirebaseFirestore mockFirestore;
    private CollectionReference mockCollectionRef;


    private ArrayList<Ingredient> pizza_list=new ArrayList<Ingredient>();

    private Ingredient mockIngredient() {
        return new Ingredient("carrot",3,"2022-11-30","pantry","lbs","vegetable");
    }

    @Before
    public void setUp() {
        // Add our mock classes
        mockFirestore = mock(FirebaseFirestore.class);
        mockCollectionRef = mock(CollectionReference.class, RETURNS_DEEP_STUBS);

        when(mockFirestore.collection(anyString()))
                .thenReturn(mockCollectionRef);

        this.RecipeController = new RecipeController(mockFirestore);
    }

    @Test
    public void testAddIngredient() {
        // Create a mock ingredient
        pizza_list.add(mockIngredient());
        Recipe pizza= new Recipe("pizza", "fastfood", "food", "", 1L, 1L, pizza_list);


        // Call our method
        RecipeController.addRecipe(pizza);

        // Capture the data value
        ArgumentCaptor<Map<String, Object>> dataCaptor = ArgumentCaptor.forClass(Map.class);

        verify(mockCollectionRef)
                .add(dataCaptor.capture());
        Map<String, Object> data = dataCaptor.getValue();

//        // Make sure the correct data was passed
//        assertEquals(data.get("Amount"), pizza.getAmount());
//        assertEquals(data.get("BestBeforeDate"), IngredientController.convertStringToTimestamp(i.getbbd()));
//        assertEquals(data.get("Category"), pizza.getCategory());
//        assertEquals(data.get("Location"), pizza.getLocation());
//        assertEquals(data.get("Name"), pizza.getName());
//        assertEquals(data.get("Unit"), pizza.getUnit());
    }
}
