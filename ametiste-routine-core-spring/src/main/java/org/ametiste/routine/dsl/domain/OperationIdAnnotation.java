package org.ametiste.routine.dsl.domain;

import org.ametiste.dynamics.foundation.elements.AnnotationSpec;
import org.ametiste.dynamics.foundation.reflection.structures.AnnotatedDescriptor;
import org.ametiste.routine.dsl.annotations.OperationId;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

/**
 *
 * @since
 */
public class OperationIdAnnotation implements AnnotationSpec {

    private final AnnotatedDescriptor annotated;

    public OperationIdAnnotation(final @NotNull AnnotatedDescriptor annotated) {
        this.annotated = annotated;
    }

    @NotNull
    @Override
    public Class<? extends Annotation> annotation() {
        return OperationId.class;
    }

}
