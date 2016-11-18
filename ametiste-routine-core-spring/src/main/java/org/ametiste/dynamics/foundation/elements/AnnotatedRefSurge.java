package org.ametiste.dynamics.foundation.elements;

import org.ametiste.dynamics.Surge;
import org.ametiste.dynamics.foundation.reflection.structures.Annotated;
import org.ametiste.dynamics.foundation.reflection.structures.Reference;

/**
 * Type alias to hold type of {@link Surge} that explodes elements
 * which {@link Annotated} as well as {@link Reference}.
 *
 * <p>Designed to hide low-level details of a surface implemenation and represent
 * a surge as a single type.
 *
 * @param <T> a type of value on which this element holds a reference
 * @param <S> a concerete element type that the {@link Annotated} as well as {@link Reference}
 *
 * @see AnnotatedRef
 * @see Annotated
 * @see Reference
 * @see Surge
 * @since 1.0
 */
interface AnnotatedRefSurge<T, S extends Annotated & Reference<T>, C> extends Surge<S, C> {

}
