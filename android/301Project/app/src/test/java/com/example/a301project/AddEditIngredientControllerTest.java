package com.example.a301project;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import android.os.Handler;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Callback;

import net.bytebuddy.asm.Advice;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("SpellCheckingInspection")
public class AddEditIngredientControllerTest {
    private AddEditIngredientController controller;
    private DocumentReference mockDocumentRef;
    private CollectionReference mockCollectionRef;
    private Task<DocumentSnapshot> mockTask;
    private DocumentSnapshot mockDocumentSnap;
    Map<String, Object> result;

    @Before
    public void setUp() {
        // Add our mock classes
        result = new HashMap<String, Object>();
        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        mockDocumentRef = mock(DocumentReference.class, RETURNS_DEEP_STUBS);
        mockCollectionRef = mock(CollectionReference.class, RETURNS_DEEP_STUBS);
        mockTask = mock(Task.class, RETURNS_DEEP_STUBS);
        mockDocumentSnap = mock(DocumentSnapshot.class, RETURNS_DEEP_STUBS);

        when(mockFirestore.collection(anyString()))
                .thenReturn(mockCollectionRef);

        when(mockFirestore.document(anyString()))
                .thenReturn(mockDocumentRef);

        when(mockDocumentRef.get())
                .thenReturn(mockTask);

        when(mockDocumentSnap.getData()).thenReturn(result);

        this.controller = new AddEditIngredientController(mockFirestore);
    }

    @Test
    public void testGetDocumentReference() {
        assertEquals(controller.getDocumentReference(), mockDocumentRef);
    }

    @Test
    public void testAddIngredientCategory() {
        String category = "Vegetables";
        controller.addIngredientCategory(category);

        ArgumentCaptor<OnSuccessListener<DocumentSnapshot>> dataCaptor = ArgumentCaptor.forClass(OnSuccessListener.class);

        verify(mockTask).addOnSuccessListener(dataCaptor.capture());
        OnSuccessListener<DocumentSnapshot> objUnderTest = dataCaptor.getValue();

        objUnderTest.onSuccess(mockDocumentSnap);
        ArrayList<String> categories = (ArrayList<String>) result.get("IngredientCategories");

        assertEquals(categories.get(0), category);
        verify(mockDocumentRef).set(result, SetOptions.merge());
    }

    @Test
    public void testAddIngredientLocation() {
        String location = "Freezer";
        controller.addIngredientLocation(location);

        ArgumentCaptor<OnSuccessListener<DocumentSnapshot>> dataCaptor = ArgumentCaptor.forClass(OnSuccessListener.class);

        verify(mockTask).addOnSuccessListener(dataCaptor.capture());
        OnSuccessListener<DocumentSnapshot> objUnderTest = dataCaptor.getValue();

        objUnderTest.onSuccess(mockDocumentSnap);
        ArrayList<String> locations = (ArrayList<String>) result.get("IngredientLocations");

        assertEquals(locations.get(0), location);
        verify(mockDocumentRef).set(result, SetOptions.merge());
    }

    @Test
    public void testAddIngredientUnit() {
        String unit = "Grams";
        controller.addIngredientUnit(unit);

        ArgumentCaptor<OnSuccessListener<DocumentSnapshot>> dataCaptor = ArgumentCaptor.forClass(OnSuccessListener.class);

        verify(mockTask).addOnSuccessListener(dataCaptor.capture());
        OnSuccessListener<DocumentSnapshot> objUnderTest = dataCaptor.getValue();

        objUnderTest.onSuccess(mockDocumentSnap);
        ArrayList<String> units = (ArrayList<String>) result.get("IngredientUnits");

        assertEquals(units.get(0), unit);
        verify(mockDocumentRef).set(result, SetOptions.merge());
    }
}
