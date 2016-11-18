package org.ametiste.dynamics.foundation.reflection.structures;

import org.ametiste.dynamics.foundation.elements._TestAnnotation;
import org.ametiste.dynamics.foundation.elements._TestAnnotationSpec;
import org.ametiste.dynamics.foundation.elements._TestEmptyType;
import org.ametiste.dynamics.foundation.elements._TestType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ClassStructureTest {

    private ClassStructure<_TestType> classStructure;
    private ClassStructure<_TestEmptyType> emptyStructure;

    private static final class _MethodWrapper {
        public _MethodWrapper(final ClassMethod method) {

        }
    }

    @Before
    public void setUp() throws Exception {
        classStructure = new ClassStructure<>(_TestType.class);
        emptyStructure = new ClassStructure<>(_TestEmptyType.class);
    }

    @Test
    public void consumeFieldsWithEmptyPattern() throws Exception {
        final Consumer<ClassField> consumer = Mockito.mock(Consumer.class);
        classStructure.fields(all -> all, consumer);
        verify(consumer, times(0)).accept(any());
    }

    @Test
    public void consumeAnnotatedFields() throws Exception {

        final ArgumentCaptor<ClassField> captor = ArgumentCaptor.forClass(ClassField.class);
        final Consumer<ClassField> consumer = Mockito.mock(Consumer.class);

        doNothing().when(consumer).accept(captor.capture());
        classStructure.fields(f -> f.hasAnnotations(_TestAnnotation.class), consumer);

        verify(consumer, times(1)).accept(any());
        assertEquals(String.class, captor.getValue().type());
        assertTrue(captor.getValue().hasAnnotations(_TestAnnotationSpec::new));
    }


    @Test
    public void consumeAllFieldsFromEmptyType() throws Exception {

        final ArgumentCaptor<ClassField> captor = ArgumentCaptor.forClass(ClassField.class);
        final Consumer<ClassField> consumer = Mockito.mock(Consumer.class);

        doNothing().when(consumer).accept(captor.capture());
        emptyStructure.fields(all -> all, consumer);

        verify(consumer, times(0)).accept(any());
    }

    @Test
    public void consumeAnnotatedFieldsFromEmptyType() throws Exception {

        final ArgumentCaptor<ClassField> captor = ArgumentCaptor.forClass(ClassField.class);
        final Consumer<ClassField> consumer = Mockito.mock(Consumer.class);

        doNothing().when(consumer).accept(captor.capture());
        emptyStructure.fields(f -> f.hasAnnotations(_TestAnnotation.class), consumer);

        verify(consumer, times(0)).accept(any());
    }


    @Test
    public void mapMethodsWithEmptyPateern() throws Exception {
        final Stream<Object> methodsStream = emptyStructure.mapMethods(all -> all, s -> null, s -> s);
        assertEquals(0, methodsStream.count());
    }

    @Test
    public void mapAnnotatedMethods() throws Exception {

        final List<_MethodWrapper> methods = classStructure.mapMethods(
                m -> m.hasAnnotations(_TestAnnotation.class), _MethodWrapper::new, s -> s).collect(Collectors.toList());

        assertEquals(3, methods.size());

        methods.forEach(
            m -> assertTrue(m.getClass().equals(_MethodWrapper.class))
        );
    }

    @Test
    public void mapAnnotatedMethodsInOrder() throws Exception {

        final List<_MethodWrapper> methods = classStructure.mapMethods(
                m -> m.hasAnnotations(_TestAnnotation.class), _MethodWrapper::new, s -> s).collect(Collectors.toList());


        // TBD

    }

    @Test
    public void qualifiedName() throws Exception {
        assertEquals(_TestType.class.getName(), classStructure.qualifiedName());
    }

    @Test
    public void type() throws Exception {
        assertEquals(_TestType.class, classStructure.type());
    }

    @Test
    public void takeAnnotationSpec() throws Exception {
        assertTrue(classStructure.annotation(_TestAnnotationSpec::new).isValid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void thereIsNoAnnotationSpec() throws Exception {
        assertFalse(emptyStructure.annotation(_TestAnnotationSpec::new).isValid());
    }

    @Test
    public void hasAnnotations() throws Exception {
        assertTrue(classStructure.hasAnnotations(_TestAnnotationSpec::new));
    }

    @Test
    public void hasNoAnnotations() throws Exception {
        assertFalse(emptyStructure.hasAnnotations(_TestAnnotationSpec::new));
    }

    @Test
    public void newInstance() throws Exception {

    }

}