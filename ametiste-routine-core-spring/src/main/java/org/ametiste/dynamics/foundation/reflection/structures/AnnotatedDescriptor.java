package org.ametiste.dynamics.foundation.reflection.structures;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.function.Function;

/**
 * Provides interface of access to annotations declared for this object class.
 *
 * <p>The descriptor provides internal interface to trace declared annotations of an element,
 * for exmaple the <i>Annotation specification</i> usually will be build over this interface.
 *
 * @see Annotated
 * @since 1.0
 */
public interface AnnotatedDescriptor {

    /**
     * Returns true if each annotation for the specified types
     * are <i>present</i> on this element, else false.
     *
     * @apiNote This method behaviour is not specified in situations where element have inherited annotations,
     * implementation must choose their own way to handle the situation.
     *
     * @param annotations the array of Class objects corresponding to the
     *        annotation types
     *
     * @return true if each annotation for the specified annotation
     *     types are present on this element, otherwise false
     *
     * @since 1.0
     */
    boolean hasAnnotations(final Class<? extends Annotation> ...annotations);

    /**
     * Applies the given transofrmation function to an annotation of the type specified by an
     * annotation parameter, and returns result of this function.
     *
     * <p>If there are no annotations <i>present</i> on this element,
     * the return value is an {@link Optional#empty()}.
     *
     * <p>The caller of this method is free to modify the returned value; it will
     * have no effect on the values returned to other callers.
     *
     * <p>This method is primarily designed to extract an annotation field values, for example this code will
     * return a value of the field <i>SomeAnnotation.someField</i>
     *
     * <code>
     *     <pre>
     *         annotatedElement.annotationValue(SomeAnnotation::someField, SomeAnnotation.class)
     *     </pre>
     * </code>
     *
     * @apiNote This method behaviour is not specified in situations where element have inherited annotations,
     * implementation must choose their own way to handle the situation.
     *
     * @param <T> a type of an annotation transform result
     * @param <A> a type of an annotation for transformation
     * @param transofrm a transformation function to be applied to a specified annotation of this element
     * @param annotation a type of annotation of this element for which transformation to be applied
     * @return an optional result of transofrm function
     * @since 1.0
     */
    <T, A extends Annotation> Optional<T> annotationValue(final Function<A, T> transofrm, final Class<A> annotation);

}
