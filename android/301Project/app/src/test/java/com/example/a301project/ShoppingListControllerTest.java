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


public class ShoppingListControllerTest {
    private ShoppingListController controller;
    private FirebaseFirestore mockFirestore;
    private CollectionReference mockCollectionRef;


    private ArrayList<ShoppingItem> shoppinglist = mock(ArrayList.class);

    private ShoppingItem mockShoppingItem() {
        return new ShoppingItem("carrot", 3.00, "2022-11-30", "pantry");
    }

    @Before
    public void setUp() {
        // Add our mock classes
        mockFirestore = mock(FirebaseFirestore.class);
        mockCollectionRef = mock(CollectionReference.class, RETURNS_DEEP_STUBS);

        when(mockFirestore.collection(anyString()))
                .thenReturn(mockCollectionRef);

        this.controller = new ShoppingListController(mockFirestore);
    }

    @Test
    public void testAddShoppingItem() {
        // Create a mock ingredient
        ShoppingItem shoppingItem = mockShoppingItem();
        shoppinglist.add(shoppingItem);

        ArgumentCaptor<ShoppingItem> argumentCaptor = ArgumentCaptor.forClass(ShoppingItem.class);

        verify(shoppinglist)
                .add(argumentCaptor.capture());

        ShoppingItem data = argumentCaptor.getValue();

        // Make sure the correct data was passed
        assertEquals(data.getAmount(), shoppingItem.getAmount());
        assertEquals(data.getCategory(), shoppingItem.getCategory());
        assertEquals(data.getName(), shoppingItem.getName());
        assertEquals(data.getUnit(), shoppingItem.getUnit());
    }
}
