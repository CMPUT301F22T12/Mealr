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

public class AddEditRecipeControllerTest {
    CollectionReference mockCollectionRef;
    DocumentReference mockDocumentRef;
    AddEditRecipeController controller;
    Task<DocumentSnapshot> mockTask;
    DocumentSnapshot mockDocumentSnap;
    Map<String, Object> result;

    @Before
    public void setUp() {
        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class, RETURNS_DEEP_STUBS);
        mockCollectionRef = mock(CollectionReference.class, RETURNS_DEEP_STUBS);
        mockDocumentRef = mock(DocumentReference.class, RETURNS_DEEP_STUBS);
        mockTask = mock(Task.class, RETURNS_DEEP_STUBS);
        mockDocumentSnap = mock(DocumentSnapshot.class, RETURNS_DEEP_STUBS);
        result = new HashMap<String, Object>();

        when(mockFirestore.collection(anyString()))
                .thenReturn(mockCollectionRef);

        when(mockFirestore.document(anyString()))
                .thenReturn(mockDocumentRef);

        when(mockDocumentRef.get())
                .thenReturn(mockTask);

        when(mockDocumentSnap.getData())
                .thenReturn(result);

        controller = new AddEditRecipeController(mockFirestore);
    }

    @Test
    public void testGetDocumentReference() {
        assertEquals(mockDocumentRef, controller.getDocumentReference());
    }

    @Test
    public void testAddRecipeCategory() {
        String category = "Healthy";
        controller.addRecipeCategory(category);

        ArgumentCaptor<OnSuccessListener<DocumentSnapshot>> dataCaptor = ArgumentCaptor.forClass(OnSuccessListener.class);

        verify(mockTask).addOnSuccessListener(dataCaptor.capture());
        OnSuccessListener<DocumentSnapshot> objUnderTest = dataCaptor.getValue();

        objUnderTest.onSuccess(mockDocumentSnap);
        ArrayList<String> categories = (ArrayList<String>) result.get("RecipeCategories");

        assertEquals(categories.get(0), category);
        verify(mockDocumentRef).set(result, SetOptions.merge());
    }
}
