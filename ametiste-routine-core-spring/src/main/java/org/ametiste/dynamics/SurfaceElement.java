package org.ametiste.dynamics;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.function.Function;

/**
 * Depicts any element of a {@link Surface}, such a dsl-operation field, a method parameter,
 * a class object, and so one;
 *
 * <p>Describes the element in a general terms of dynamic element (methods of the interface
 * are defines these general terms).
 *
 * <p>Each element allowed to have one or more {@link DynamicSurfaceStructure} as structure of an internal
 * surface of element. The internal surface of element is regular {@link Surface} but defined for
 * the element internals ( for example, internal surface of the element may have their own elements ).
 *
 * @since 1.0
 * @see Surface
 */
public interface SurfaceElement {

    /**
     * Returns an object as the result of applying the given {@code transform} function to
     * the element name.
     *
     * <p>Note, this method does not impact the state of the element.
     *
     * @param transform the function to transform the element name
     * @param <T> type of transform result
     *
     * @return object of the type {@code T}, can't be null.
     * @since 1.0
     */
    <T> Optional<T> mapName(Function<String, T> transform);

    /**
     * Returns an object as the result of applying the given {@code transform} function to
     * the element feature.
     *
     * <p>If a feature is not defined for the element this method will return {@link Optional#empty()}.
     *
     * <p>Note, this method does not impact the state of the element.
     *
     * @param transform the function to transform the element feature
     * @param feature type of target feature
     * @param <T> type of transform result
     *
     * @return object of the type {@code T}, can be null.
     * @since 1.0
     */
    <F, T> Optional<T> mapFeature(Function<F, T> transform, Class<F> feature);



    /**
     * Checks that the element has the defined structure. Please see {@link DynamicSurfaceStructure}
     * for details.
     *
     * @param structure a structure to be checked against element surface
     * @return {@code true} if the element has the given {@code structure}, otherwise {@code false}.*
     * @see DynamicSurfaceStructure
     */
    boolean hasStructureOf(DynamicSurfaceStructure structure);

    /**
     * Checks that the element has declared {@code annotation}.
     *
     * @param annotation annotation type to be checked
     *
     * @return {@code true} if the element is annotated by the given {@code annotation}, otherwise {@code false}.
     * @since 1.0
     */
    boolean isAnnotatedBy(Class<? extends Annotation> annotation);

    /**
     * Returns value of an {@code annotation} that declared for the element. This value is extracted
     * by the given {@code valueProvider} function (usually, an accessor method of the {@code annotation}):
     *
     * <p>
     * <code>
     *     <pre>
     *     element.annotationValue(MyAnnotation.class, MyAnnotation:requestedValue);
     *     </pre>
     * </code>
     *
     * @param annotation annotation type which value is requested
     * @param valueProvider transformation
     *
     * @return {@code true} if the element is annotated by the given {@code annotation}, otherwise {@code false}.
     * @since 1.0
     */
    <S extends Annotation, T> T annotationValue(Class<S> annotation, Function<S, T> valueProvider);

}
