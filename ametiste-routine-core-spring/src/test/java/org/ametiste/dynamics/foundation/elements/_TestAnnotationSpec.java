package org.ametiste.dynamics.foundation.elements;

import org.ametiste.dynamics.foundation.reflection.structures.AnnotatedDescriptor;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class _TestAnnotationSpec implements AnnotationSpec {

    private final AnnotatedDescriptor descriptor;

    public _TestAnnotationSpec() {
        this(new AnnotatedDescriptor() {
            @Override
            public boolean hasAnnotations(final Class<? extends Annotation>... annotations) {
                return true;
            }

            @Override
            public <T, A extends Annotation> Optional<T> annotationValue(final Function<A, T> transofrm, final Class<A> annotation) {
                return Optional.empty();
            }
        });
    }

    public _TestAnnotationSpec(AnnotatedDescriptor descriptor) {

        if (!descriptor.hasAnnotations(_TestAnnotation.class)) {
            throw new IllegalArgumentException("There is no valid combintation of annotations on this class.");
        }

        this.descriptor = descriptor;
    }

    public boolean isValid() {
        return true;
    }

    public void assertNotValue(String value) {
        assertNotEquals(value, descriptor.annotationValue(_TestAnnotation::eqValue,
                _TestAnnotation.class).orElse(null));
    }

    public void assertValue(String value) {
        assertEquals(value, descriptor.annotationValue(_TestAnnotation::eqValue,
                _TestAnnotation.class).orElse(null));
    }

    @NotNull
    @Override
    public Class<? extends Annotation> annotation() {
        return _TestAnnotation.class;
    }
}
