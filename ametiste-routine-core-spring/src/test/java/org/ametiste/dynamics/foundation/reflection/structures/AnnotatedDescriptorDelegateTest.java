package org.ametiste.dynamics.foundation.reflection.structures;

import org.ametiste.dynamics.foundation.elements._TestAnnotation;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnnotatedDescriptorDelegateTest {

    @Test
    public void hasAnnotationsInImplementingForm() throws Exception {

        final AnnotatedDescriptorDelegate delegate = new AnnotatedDescriptorDelegate(
                annotations -> annotations.anyMatch(a -> a.isAssignableFrom(_TestAnnotation.class)),
                (a, f) -> Optional.empty()
        );

        assertTrue(delegate.hasAnnotations(_TestAnnotation.class));
    }

    @Test
    public void hasNoAnnotationsInImplementingForm() throws Exception {

        final AnnotatedDescriptorDelegate delegate = new AnnotatedDescriptorDelegate(
                annotations -> annotations.anyMatch(a -> a.isAssignableFrom(Test.class)),
                (a, f) -> Optional.empty()
        );

        assertFalse(delegate.hasAnnotations(_TestAnnotation.class));
    }

    @Test
    public void hasAnnotationsInMatchingForm() throws Exception {

        final AnnotatedDescriptorDelegate delegate = new AnnotatedDescriptorDelegate(
                annotation -> annotation.isAssignableFrom(_TestAnnotation.class),
                a -> mock(_TestAnnotation.class)
        );

        assertTrue(delegate.hasAnnotations(_TestAnnotation.class));
    }

    @Test
    public void hasNoAnnotationsInMatchingForm() throws Exception {

        final AnnotatedDescriptorDelegate delegate = new AnnotatedDescriptorDelegate(
                annotation -> annotation.isAssignableFrom(Test.class),
                a -> mock(Test.class)
        );

        assertFalse(delegate.hasAnnotations(_TestAnnotation.class));
    }

    @Test
    public void annotationValueInMatchingForm() throws Exception {

        final _TestAnnotation mock = mock(_TestAnnotation.class);

        when(mock.eqValue()).thenReturn("TEST-VALUE");

        final AnnotatedDescriptorDelegate delegate = new AnnotatedDescriptorDelegate(
                annotation -> annotation.isAssignableFrom(_TestAnnotation.class),
                a -> mock
        );

        assertEquals("TEST-VALUE",
                delegate.annotationValue(_TestAnnotation::eqValue, _TestAnnotation.class).orElse("FAILED-TEST"));

    }

    @Test
    public void annotationHasNoValueInMatchingForm() throws Exception {

        final _TestAnnotation mock = mock(_TestAnnotation.class);

        when(mock.eqValue()).thenReturn(null);

        final AnnotatedDescriptorDelegate delegate = new AnnotatedDescriptorDelegate(
                annotation -> annotation.isAssignableFrom(_TestAnnotation.class),
                a -> mock
        );

        assertEquals("NON-VALUE",
                delegate.annotationValue(_TestAnnotation::eqValue, _TestAnnotation.class).orElse("NON-VALUE"));

    }

    @Test
    public void annotationValueInImplementingForm() throws Exception {

        final AnnotatedDescriptorDelegate delegate = new AnnotatedDescriptorDelegate(
                annotations -> annotations.anyMatch(a -> a.isAssignableFrom(Test.class)),
                (a, f) -> Optional.of("TEST-VALUE")
        );

        assertFalse(delegate.hasAnnotations(_TestAnnotation.class));

        assertEquals("TEST-VALUE",
                delegate.annotationValue(_TestAnnotation::eqValue, _TestAnnotation.class).orElse("FAILED-TEST"));

    }

    @Test
    public void annotationHasNoValueInImplementingForm() throws Exception {

        final AnnotatedDescriptorDelegate delegate = new AnnotatedDescriptorDelegate(
                annotations -> annotations.anyMatch(a -> a.isAssignableFrom(Test.class)),
                (a, f) -> Optional.empty()
        );

        assertFalse(delegate.hasAnnotations(_TestAnnotation.class));

        assertEquals("NON-VALUE",
                delegate.annotationValue(_TestAnnotation::eqValue, _TestAnnotation.class).orElse("NON-VALUE"));

    }

}