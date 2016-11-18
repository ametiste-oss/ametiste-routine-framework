package org.ametiste.dynamics.foundation.elements;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AnnotatedRefTest {

    @Mock
    private AnnotatedRefFeature<_TestType> mockFeature;

    private AnnotatedRef<_TestType> annotatedRef;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(mockFeature.ofType(_TestType.class)).thenReturn(true);
        when(mockFeature.type()).thenReturn(_TestType.class);
        when(mockFeature.hasAnnotations(any())).thenReturn(true);
        when(mockFeature.<_TestAnnotationSpec>annotation(any())).thenReturn(new _TestAnnotationSpec());

        annotatedRef = new AnnotatedRef<>(mockFeature);
    }

    @Test
    public void isTrueRefeneceTo() throws Exception {
        assertTrue(annotatedRef.isRefeneceTo(_TestType.class));
    }

    @Test
    public void isFalseRefeneceTo() throws Exception {
        assertFalse(annotatedRef.isRefeneceTo(_UndefinedType.class));
    }

    @Test
    public void provideValueOfvalidType() throws Exception {
        final _TestType ref = new _TestType();
        annotatedRef.provideValue(ref);
        verify(mockFeature).referencesTo(ref);
    }

    @Test
    public void type() throws Exception {
        assertEquals(_TestType.class, annotatedRef.type());
    }

    @Test
    public void annotation() throws Exception {
        // TODO: how can I test such functions? At the moment mock accepts any function
        assertTrue(annotatedRef.annotation(d -> new _TestAnnotationSpec()).isValid());
    }

}