package org.ametiste.dynamics;

/**
 * Special type of {@link Surface} that enclosed by the {@link Object} and a sub-surface
 * that depicted by the given structure.
 *
 * <p> This kind of surfaces have to use in applications where only a sub-surface
 * structure is known or have sense. Also it may be used as a shortcut to a surface definition.
 *
 * <p> This surface does not have any special methods.
 *
 * @param <S> a structure of the surface.
 *
 * @see Surface
 * @see RightSurface
 * @since 1.0
 */
public interface RightSurface<S> extends Surface<Object, S> { }
