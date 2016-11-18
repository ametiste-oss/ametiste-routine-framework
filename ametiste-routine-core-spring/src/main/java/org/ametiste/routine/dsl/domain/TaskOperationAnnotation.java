package org.ametiste.routine.dsl.domain;

import org.ametiste.dynamics.foundation.elements.AnnotationSpec;
import org.ametiste.dynamics.foundation.reflection.structures.AnnotatedDescriptor;
import org.ametiste.routine.dsl.annotations.TaskOperation;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.function.Supplier;

/**
 *
 * @since
 */
public class TaskOperationAnnotation implements AnnotationSpec {

    private final AnnotatedDescriptor annotated;

    public TaskOperationAnnotation(final @NotNull AnnotatedDescriptor annotated) {
        this.annotated = annotated;
    }

    @NotNull
    public <T extends Throwable> int orderOrThrow(final @NotNull Supplier<T> exception) throws T {
        return annotated.annotationValue(TaskOperation::order, TaskOperation.class)
                .orElseThrow(exception);
    }

    @NotNull
    @Override
    public Class<? extends Annotation> annotation() {
        return TaskOperation.class;
    }
}
