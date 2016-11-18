package org.ametiste.dynamics;

/**
 * Special type of {@link Surface} that enclosed by the given structure and has a sub-surface
 * that depicted by the {@link Object} structure.
 *
 * <p> This kind of surfaces have to use in applications where only an enclosed
 * structure is known or have sense. Also it may be used as shortcut to a surface definition.
 *
 * <p> This surface does not have any special methods.
 *
 * @param <E> a structure of the enclosing surface.
 * @implNote at the moment of 1.0 there is no internal usage for this kind of a surface, it's defined
 * to complete the surface structure definition.
 *
 * @see Surface
 * @see LeftSurface
 * @since 1.0
 */
public interface LeftSurface<E> extends Surface<E, Object> {}
