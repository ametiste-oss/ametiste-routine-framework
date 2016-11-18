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

import java.lang.annotation.*;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClassMethodTest {

    private ClassMethod classMethod;

    private static class UndefinedSpec implements AnnotationSpec {

        public UndefinedSpec(AnnotatedDescriptor annotatedDescriptor) { }

        @NotNull
        @Override
        public Class<? extends Annotation> annotation() {
            return Test.class;
        }

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER})
    private @interface TestMethodParam {

    }

    private static class JustToGetMethodType {

        @_TestAnnotation(eqValue = "JUST-METHOD-TEST")
        public void testMethod(@TestMethodParam String one) {
            assertEquals("TEST", one);
        }

    }

    @Mock
    private ClassStructure<_TestType> classStructure;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(classStructure.qualifiedName()).thenReturn(JustToGetMethodType.class.getSimpleName());

        // NOTE: there is only one method at JustToGetMethodType, so we just peek the first
        classMethod = new ClassMethod(classStructure, JustToGetMethodType.class.getDeclaredMethods()[0]);
    }

    @Test
    public void qualifiedName() throws Exception {
        assertEquals(JustToGetMethodType.class.getSimpleName() + "#testMethod", classMethod.qualifiedName());
    }

    @Test
    public void name() throws Exception {
        assertEquals("testMethod", classMethod.name());
    }

    @Test
    public void invokeSuccessfully() throws Exception {

        final ObjectInstance objectInstance = new ObjectInstance<JustToGetMethodType>(
                new JustToGetMethodType(), new ClassStructure<>(JustToGetMethodType.class)
        );

        // NOTE: assertion will be applied by JustToGetMethodType#testMethod, so in a case of any error
        // RuntimeException enclosing AssertionError will be thrown
        classMethod.invoke(objectInstance,
                p -> p.hasAnnotations(TestMethodParam.class),
                p -> {
                    p.referencesTo("TEST");
                    return p;
                }
        );

    }

    @Test(expected = RuntimeException.class)
    public void invokeErrorProne() throws Exception {

        final ObjectInstance objectInstance = new ObjectInstance<JustToGetMethodType>(
                new JustToGetMethodType(), new ClassStructure<>(JustToGetMethodType.class)
        );

        // NOTE: assertion will be applied by JustToGetMethodType#testMethod, so in a case of any error
        // RuntimeException enclosing AssertionError will be thrown
        classMethod.invoke(objectInstance,
                p -> p.hasAnnotations(TestMethodParam.class),
                p -> {
                    p.referencesTo("NOT-MATCHED");
                    return p;
                }
        );

    }

    @Test
    public void fieldAnnotationValue() throws Exception {
        classMethod.annotation(_TestAnnotationSpec::new).assertNotValue("NOT-THIS-VALUE");
    }

    @Test
    public void fieldAnnotationValueNotMatched() throws Exception {
        classMethod.annotation(_TestAnnotationSpec::new).assertValue("JUST-METHOD-TEST");
    }

    @Test
    public void hasAnnotations() throws Exception {
        assertTrue(classMethod.hasAnnotations(_TestAnnotationSpec::new));
    }

    @Test
    public void hasNoAnnotations() throws Exception {
        assertFalse(classMethod.hasAnnotations(UndefinedSpec::new));
    }

}