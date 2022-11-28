package com.example.a301project;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings("SpellCheckingInspection")
public class IngredientControllerTest {
    private IngredientController controller;
    private CollectionReference mockCollectionRef;

    private Ingredient mockIngredient() {
        return new Ingredient("Carrot",3,"2022-11-30","Pantry","lbs","Vegetable");
    }

    @Before
    public void setUp() {
        // Add our mock classes
        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        mockCollectionRef = mock(CollectionReference.class, RETURNS_DEEP_STUBS);

        when(mockFirestore.collection(anyString()))
                .thenReturn(mockCollectionRef);

        this.controller = new IngredientController(mockFirestore);
    }

    @Test
    public void testConvertStringToTimestamp() {
        Date date = new GregorianCalendar(2022, Calendar.DECEMBER, 9).getTime();
        Timestamp stamp = new Timestamp(date);

        assertEquals(IngredientController.convertStringToTimestamp("2022-12-09"), stamp);

    }

    @Test
    public void testGetCollectionReference() {
        assertEquals(controller.getCollectionReference(), mockCollectionRef);
    }

    @Test
    public void testAddIngredient() {
        // Create a mock ingredient
        Ingredient i = mockIngredient();

        // Call our method
        controller.addIngredient(i);

        // Capture the data value
        ArgumentCaptor<Map<String, Object>> dataCaptor = ArgumentCaptor.forClass(Map.class);

        verify(mockCollectionRef)
                .add(dataCaptor.capture());
        Map<String, Object> data = dataCaptor.getValue();

        // Make sure the correct data was passed
        assertEquals(data.get("Amount"), i.getAmount());
        assertEquals(data.get("BestBeforeDate"), IngredientController.convertStringToTimestamp(i.getbbd()));
        assertEquals(data.get("Category"), i.getCategory());
        assertEquals(data.get("Location"), i.getLocation());
        assertEquals(data.get("Name"), i.getName());
        assertEquals(data.get("Unit"), i.getUnit());
    }

    @Test
    public void testRemoveIngredient() {
        Ingredient i = mockIngredient();
        i.setId("TEST_ID");

        // add, then remove ingredient
        controller.addIngredient(i);
        controller.removeIngredient(i);

        // verify delete was called with the correct ID
        assertEquals(i.getId(), "TEST_ID");
        verify(mockCollectionRef.document(i.getId())).delete();
    }

    @Test
    public void testNotifyUpdate() {
        Ingredient i = mockIngredient();
        i.setId("TEST_ID");
        controller.addIngredient(i);
        Ingredient u = new Ingredient("Pineapple", 2.0, "2022-03-13", "Cupboard", "lbs", "Fruit");
        u.setId("TEST_ID");

        controller.notifyUpdate(u);

        ArgumentCaptor<Map<String, Object>> dataCaptor = ArgumentCaptor.forClass(Map.class);

        verify(mockCollectionRef.document(i.getId())).update(dataCaptor.capture());

        Map<String, Object> updatedIngredient = dataCaptor.getValue();

        // check to see if all values have been updated
        assertEquals("TEST_ID", u.getId());
        assertEquals("Pineapple", updatedIngredient.get("Name"));
        assertEquals(2.0, updatedIngredient.get("Amount"));
        assertEquals(IngredientController.convertStringToTimestamp("2022-03-13"), updatedIngredient.get("BestBeforeDate"));
        assertEquals("Fruit", updatedIngredient.get("Category"));
        assertEquals("lbs", updatedIngredient.get("Unit"));
        assertEquals("Cupboard", updatedIngredient.get("Location"));
    }
}
