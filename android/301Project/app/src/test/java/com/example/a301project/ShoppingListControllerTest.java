package com.example.a301project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class ShoppingListControllerTest {
    private FirebaseFirestore mockFirestore;
    private CollectionReference mockCollectionRef;
    private ShoppingListController controller;
    private ArrayList<ShoppingItem> mockMealPlanItemDataList;
    private ArrayList<ShoppingItem> mockIngredientStorageItemsDataList;
    private ArrayList<Recipe> mockMealPlanRecipeDataList;
    private ArrayList<Recipe> mockRecipeItemsDataList;
    ShoppingListController.shoppingItemSuccessHandler mockS;

    private Ingredient mockIngredient() {
        return new Ingredient("Carrot", 2.00, "2022-09-16", "Cupboard", "Kgs", "Vegetable");
    }
    private ShoppingItem mockShoppingItem() {
        return new ShoppingItem("Carrot", 3.00, "Kgs", "Vegetable");
    }
    private ShoppingItem mockShoppingItem2() {
        return new ShoppingItem("Carrot", 4.00, "Kgs", "Vegetable");
    }
    private Recipe mockRecipe() {
        ArrayList<Ingredient> i = new ArrayList<Ingredient>();
        i.add(mockIngredient());
        return new Recipe("Carrot Soup", "Vegetable", "Food", "", 1L, 2L, i);
    }

    @Before
    public void setUp() {
        // Add our mock classes
        mockFirestore = mock(FirebaseFirestore.class);
        mockCollectionRef = mock(CollectionReference.class, RETURNS_DEEP_STUBS);

        when(mockFirestore.collection(anyString()))
                .thenReturn(mockCollectionRef);

        mockS = mock(ShoppingListController.shoppingItemSuccessHandler.class, RETURNS_DEEP_STUBS);

        mockMealPlanItemDataList = new ArrayList<ShoppingItem>();
        mockIngredientStorageItemsDataList = new ArrayList<ShoppingItem>();
        mockMealPlanRecipeDataList = new ArrayList<Recipe>();
        mockRecipeItemsDataList = new ArrayList<Recipe>();
    }

    @Test
    public void testGetShoppingItemsInIngredientStorageHasEnough() {
        ShoppingItem item1 = mockShoppingItem();
        ShoppingItem item2 = mockShoppingItem2();

        mockMealPlanItemDataList.add(item1);
        mockIngredientStorageItemsDataList.add(item2);

        this.controller = new ShoppingListController(mockFirestore, mockMealPlanItemDataList,
                mockIngredientStorageItemsDataList, mockMealPlanRecipeDataList, mockRecipeItemsDataList);

        controller.getShoppingItems(mockS);

        ArgumentCaptor<ArrayList<ShoppingItem>> dataCaptor = ArgumentCaptor.forClass(ArrayList.class);

        verify(mockS).f(dataCaptor.capture());

        // if there is enough in ingredient storage, make sure it was not added to shopping list
        assertEquals(0, dataCaptor.getValue().size());
    }

    @Test
    public void testGetShoppingItemsInIngredientStorageNotEnough() {
        ShoppingItem item2 = mockShoppingItem();
        ShoppingItem item1 = mockShoppingItem2();

        mockMealPlanItemDataList.add(item1);
        mockIngredientStorageItemsDataList.add(item2);

        this.controller = new ShoppingListController(mockFirestore, mockMealPlanItemDataList,
                mockIngredientStorageItemsDataList, mockMealPlanRecipeDataList, mockRecipeItemsDataList);

        controller.getShoppingItems(mockS);

        ArgumentCaptor<ArrayList<ShoppingItem>> dataCaptor = ArgumentCaptor.forClass(ArrayList.class);

        verify(mockS).f(dataCaptor.capture());

        // if there is not enough in ingredient storage, test to make sure the amount was added to shopping list
        assertEquals("Carrot", dataCaptor.getValue().get(0).getName());
        assertEquals(1.00, dataCaptor.getValue().get(0).getAmount(), 0.00);
        assertEquals("Kgs", dataCaptor.getValue().get(0).getUnit());
        assertEquals("Vegetable", dataCaptor.getValue().get(0).getCategory());
    }

    @Test
    public void testGetShoppingItemsNotInIngredientStorage() {
        ShoppingItem item1 = mockShoppingItem();

        mockMealPlanItemDataList.add(item1);

        this.controller = new ShoppingListController(mockFirestore, mockMealPlanItemDataList,
                mockIngredientStorageItemsDataList, mockMealPlanRecipeDataList, mockRecipeItemsDataList);

        controller.getShoppingItems(mockS);

        ArgumentCaptor<ArrayList<ShoppingItem>> dataCaptor = ArgumentCaptor.forClass(ArrayList.class);

        verify(mockS).f(dataCaptor.capture());

        // assert the new item was added to shopping list
        assertEquals("Carrot", dataCaptor.getValue().get(0).getName());
        assertEquals(3.00, dataCaptor.getValue().get(0).getAmount(), 0.00);
        assertEquals("Kgs", dataCaptor.getValue().get(0).getUnit());
        assertEquals("Vegetable", dataCaptor.getValue().get(0).getCategory());
    }

    @Test
    public void testGetIngredientsFromMealPlanRecipes() {
        mockMealPlanRecipeDataList.add(mockRecipe());
        mockRecipeItemsDataList.add(mockRecipe());

        this.controller = new ShoppingListController(mockFirestore, mockMealPlanItemDataList,
                mockIngredientStorageItemsDataList, mockMealPlanRecipeDataList, mockRecipeItemsDataList);

        // since this method is private, use getShoppingItems to call it
        controller.getShoppingItems(mockS);

        // assert that the ingredient was added and that the amount has been multiplied with the
        // number of servings (2 * 2 = 4)
        assertEquals(1, mockMealPlanItemDataList.size());
        assertEquals("Carrot", mockMealPlanItemDataList.get(0).getName());
        assertEquals(4.00, mockMealPlanItemDataList.get(0).getAmount(), 0.00);
        assertEquals("Vegetable", mockMealPlanItemDataList.get(0).getCategory());
        assertEquals("Kgs", mockMealPlanItemDataList.get(0).getUnit());
    }
}
