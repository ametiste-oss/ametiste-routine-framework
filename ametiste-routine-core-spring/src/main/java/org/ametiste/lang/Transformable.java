package org.ametiste.lang;

import java.util.Optional;
import java.util.function.Function;

/**
 * Defines an object that may be transformed from a type of T to a type of R by
 * some function.
 *
 * @since 1.0
 */
public interface Transformable<T> {

    /**
     * Transforms value that of type {@code T} that belongs to this object
     * by the given {@code transformation}.
     *
     * @param transformation function to transform this object's value to a type of {@code R}
     * @param <R> result type of transformation
     * @return result of transformation, can't be null.
     */
    <R> R map(Function<T, R> transformation);

}
