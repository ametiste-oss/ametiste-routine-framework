package org.ametiste.dynamics.foundation.reflection.structures;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

/**
 * Defines element role that generalize the concept of an element that holds or provides
 * reference to other element of a runtime. For example such elements as
 * the {@link Parameter}, the {@link Field}.
 *
 * @since 1.0
 */
public interface Reference<T> {

    /**
     * Checks that this reference is the reference of the given type.
     *
     * @param type a type to check this reference
     * @return {@code true} if this element is the reference on the type; otherwise {@code false}.
     * @since 1.0
     */
    boolean ofType(@NotNull Class<?> type);

    /**
     * Returns a type on which this element holds a reference.
     *
     * @return a type of this reference, can't be null
     * @since 1.0
     */
    @NotNull
    Class<T> type();

    /**
     * Sets a reference value that represented by this element to
     * the reference on the given element.
     *
     * @param ref a reference to the given element
     * @since 1.0
     */
    void referencesTo(@NotNull final T ref);

}
