package org.ametiste.dynamics.foundation.reflection.structures;

import org.ametiste.dynamics.SurfaceFeature;
import org.ametiste.dynamics.SurfaceStructure;
import org.ametiste.dynamics.foundation.elements.AnnotationSpec;
import org.hibernate.annotations.common.annotationfactory.AnnotationDescriptor;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.function.Function;

/**
 * Represents an element of an object graph that have or could have declared {@link Annotation}s.
 *
 * <p>In fact, each element of the runtime may have declared annotations, so this interface generalize
 * the concept of declarations and provides, in some sense, convenient way to manipulate over such
 * declarations.
 *
 * <p>The annotated object uses an <i>annotation specification</i> represented as an {@link AnnotationSpec} object.
 *
 * <p>Indirect each annotated object provides an {@link AnnotatedDescriptor}, the interface
 * of access to declared annotations. <i>Annotation specifications</i> usually will be build over such descriptors.
 *
 * @see AnnotatedDescriptor
 * @see AnnotationSpec
 * @since 1.0
 */
@SurfaceStructure
public interface Annotated {

    /**
     * Applies the given {@code spec} function to an internal {@link AnnotationDescriptor} of this object
     * to obtain an {@link AnnotationSpec}.
     *
     * @param spec a function to build an annotation specification, can't be null
     * @param <T> a type of annotation scpefication to be built
     *
     *
     * @return the annotation specification built over the internal annotation descriptor, can't be null.
     * @since 1.0
     */
    @NotNull
    @SurfaceFeature
    //todo: may be this method should return Optional of spec, what to do if there is no way to build spec?
    <T extends AnnotationSpec> T annotation(@NotNull Function<AnnotatedDescriptor, T> spec);

    /**
     * Applies the given {@code spec} function to an internal {@link AnnotationDescriptor} of this object
     * to obtain a specification and check that this annoted object contains an annotation that
     * conforms to this specification.
     *
     * @param spec a function to build an annotation specification, can't be null
     *
     * @return {@code true} if this annotated object has the specified annotation, otherwise {@code false}.
     * @since 1.0
     */
    @SurfaceFeature
    boolean hasAnnotations(@NotNull Function<AnnotatedDescriptor, ? extends AnnotationSpec> spec);

}
