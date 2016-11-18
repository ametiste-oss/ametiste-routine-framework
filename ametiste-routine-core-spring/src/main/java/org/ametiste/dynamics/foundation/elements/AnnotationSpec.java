package org.ametiste.dynamics.foundation.elements;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

/**
 * Represents <i>annotation specification</i>s to manipulate over an annotation in the standard and defined manner.
 *
 * <p>In opposite to the {@link Annotation} that only a signature definition (or interface),
 * the {@link AnnotationSpec} contains of logic to obtain, validate and provide values that bound to the annotation.
 *
 * @since 1.0
 */
public interface AnnotationSpec {

    /**
     * Returns a type of annotation to which this specifiation is bound.
     *
     * @return a type of specified annotation, can't be null
     * @since 1.0
     */
    @NotNull
    Class<? extends Annotation> annotation();

}
