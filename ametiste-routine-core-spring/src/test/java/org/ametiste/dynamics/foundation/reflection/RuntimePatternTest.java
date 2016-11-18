package org.ametiste.dynamics.foundation.reflection;

import org.ametiste.dynamics.foundation.elements._TestAnnotation;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class RuntimePatternTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void fromEmpty() throws Exception {
        RuntimePattern.create(p -> p.hasAnnotations(_TestAnnotation.class))
            .hasAnnotations(Test.class)
            .let(l -> assertTrue(l.containsAll(Arrays.asList(_TestAnnotation.class, Test.class))));
    }

    @Test
    public void hasAnnotation() throws Exception {
        RuntimePattern.create(p -> p.hasAnnotations(_TestAnnotation.class))
            .let(l -> assertTrue(l.contains(_TestAnnotation.class)));
    }

    @Test
    public void hasMultipleAnnotations() throws Exception {
        RuntimePattern.create(
                p -> p.hasAnnotations(_TestAnnotation.class, Test.class)
        ).let(l -> assertTrue(l.containsAll(Arrays.asList(_TestAnnotation.class, Test.class))));
    }

    @Test
    public void hasMultipleAnnotationsStacks() throws Exception {
        RuntimePattern.create(
                p -> p.hasAnnotations(_TestAnnotation.class)
        ).hasAnnotations(
                Test.class
        ).let(l -> assertTrue(l.containsAll(Arrays.asList(_TestAnnotation.class, Test.class))));
    }

    @Test
    public void hasMultipleAnnotationsStacksUniqueue() throws Exception {
        RuntimePattern.create(
                p -> p.hasAnnotations(_TestAnnotation.class, Test.class)
        ).hasAnnotations(
                Test.class
        ).let(l -> {
            assertTrue(l.size() == 2);
            assertTrue(l.containsAll(Arrays.asList(_TestAnnotation.class, Test.class)));
        });
    }

    @Test
    public void hasMultipleAnnotationsAllowsUniqueueDuringInit() throws Exception {
        RuntimePattern.create(
                p -> p.hasAnnotations(_TestAnnotation.class, _TestAnnotation.class)
        ).let(l -> {
            assertEquals(1, l.size());
            assertTrue(l.contains(_TestAnnotation.class));
        });
    }

    @Test
    public void letListOfAnnotations() throws Exception {
        RuntimePattern.create(
                p -> p.hasAnnotations(_TestAnnotation.class, Test.class)
        ).let(l -> assertTrue(l.containsAll(Arrays.asList(_TestAnnotation.class, Test.class))));
    }

    @Test
    public void mapListOfAnnotations() throws Exception {
        final List<Class<? extends Annotation>> list = RuntimePattern.create(
                p -> p.hasAnnotations(_TestAnnotation.class, Test.class)
        ).map(l -> l);
        assertTrue(list.containsAll(Arrays.asList(_TestAnnotation.class, Test.class)));
    }

}