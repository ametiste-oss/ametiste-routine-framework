package org.ametiste.dynamics;

import org.ametiste.routine.dsl.annotations.OperationParameter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Model element that represents a process of a {@link Surface} modification
 * as some kind of "explosion" over the surface.
 *
 * <p> Modifies a {@link Surface} of the given type ( {@link RightSurface} to be honest )
 * in a {@code context} of the given type.
 *
 * <p> From the technical point of view, implementations of this interface
 * allow to define various mechanism to mutate runtime object graph.
 *
 * @param <C> defines a type of a context in which surge explodes
 * @param <S> defines a type of a surface structure where surge may happend
 *
 * @see Surface
 * @see RightSurface
 *
 * @since 1.0
 */
public interface Surge<S, C> {

    void explode(@NotNull RightSurface<S> element, @NotNull C context);

}
