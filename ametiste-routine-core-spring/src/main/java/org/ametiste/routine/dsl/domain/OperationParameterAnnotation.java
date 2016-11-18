package org.ametiste.routine.dsl.domain;

import org.ametiste.dynamics.foundation.elements.AnnotationSpec;
import org.ametiste.dynamics.foundation.reflection.structures.AnnotatedDescriptor;
import org.ametiste.routine.dsl.annotations.OperationParameter;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.function.Supplier;

/**
 *
 * @since
 */
public class OperationParameterAnnotation implements AnnotationSpec {

    private final AnnotatedDescriptor annotated;

    public OperationParameterAnnotation(final @NotNull AnnotatedDescriptor annotated) {
        this.annotated = annotated;
    }

    public <T extends Throwable> String nameOrThrow(final Supplier<T> exception) throws T {
        return annotated.annotationValue(OperationParameter::value, OperationParameter.class)
                .filter(name -> !name.isEmpty())
                .orElseThrow(exception);
    }

    @NotNull
    @Override
    public Class<? extends Annotation> annotation() {
        return OperationParameter.class;
    }

}
