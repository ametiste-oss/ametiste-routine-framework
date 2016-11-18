package org.ametiste.dynamics.foundation.reflection.structures;

import org.ametiste.dynamics.foundation.elements.AnnotationSpec;
import org.ametiste.dynamics.foundation.elements._TestAnnotation;
import org.ametiste.dynamics.foundation.elements._TestAnnotationSpec;
import org.ametiste.dynamics.foundation.elements._TestType;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.annotation.Annotation;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class ClassFieldTest {

    private static class UndefinedSpec implements AnnotationSpec {

        public UndefinedSpec(AnnotatedDescriptor annotatedDescriptor) { }

        @NotNull
        @Override
        public Class<? extends Annotation> annotation() {
            return Test.class;
        }

    }

    private static class JustToGetFieldType {

        @_TestAnnotation(eqValue = "JUST-FIELD-TEST")
        public _TestType testTypeField;

    }

    @Mock
    private ClassStructure<_TestType> classStructure;

    private ClassField classField;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(classStructure.type()).thenReturn(_TestType.class);

        // NOTE: there is only one field at JustToGetFieldType, so we just peek the first
        classField = new ClassField(classStructure, JustToGetFieldType.class.getDeclaredFields()[0]);
    }

    @Test
    public void typeOfField() throws Exception {
        assertEquals(_TestType.class, classField.type());
    }

    @Test
    public void fieldAnnotationValue() throws Exception {
        classField.annotation(_TestAnnotationSpec::new).assertNotValue("NOT-THIS-VALUE");
    }

    @Test
    public void fieldAnnotationValueNotMatched() throws Exception {
        classField.annotation(_TestAnnotationSpec::new).assertValue("JUST-FIELD-TEST");
    }

    @Test
    public void hasAnnotations() throws Exception {
        assertTrue(classField.hasAnnotations(_TestAnnotationSpec::new));
    }

    @Test
    public void hasNoAnnotations() throws Exception {
        assertFalse(classField.hasAnnotations(UndefinedSpec::new));
    }

}