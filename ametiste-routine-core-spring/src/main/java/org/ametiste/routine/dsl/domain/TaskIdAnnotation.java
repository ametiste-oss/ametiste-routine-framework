package org.ametiste.routine.dsl.domain;

import org.ametiste.dynamics.foundation.elements.AnnotationSpec;
import org.ametiste.dynamics.foundation.reflection.structures.AnnotatedDescriptor;
import org.ametiste.routine.dsl.annotations.TaskId;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

/**
 *
 * @since
 */
public class TaskIdAnnotation implements AnnotationSpec {

    public static final String ASSERT_MESSAGE = "@" + TaskId.class.getSimpleName() +
            "element must have valueType of java.util.UUID or java.lang.String.";

    private final AnnotatedDescriptor annotated;

    public TaskIdAnnotation(final @NotNull AnnotatedDescriptor annotated) {
        this.annotated = annotated;
    }

    @NotNull
    @Override
    public Class<? extends Annotation> annotation() {
        return TaskId.class;
    }

}
