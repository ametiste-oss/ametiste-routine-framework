package org.ametiste.lang;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 *
 * @see Transformable
 * @since
 */
public interface BiTransformable<T, U> {

    <R> R map(BiFunction<T, U, R> transformation);

}
