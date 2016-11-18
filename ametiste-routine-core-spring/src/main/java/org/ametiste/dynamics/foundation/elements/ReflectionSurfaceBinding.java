package org.ametiste.dynamics.foundation.elements;

import org.ametiste.dynamics.Planar;
import org.ametiste.dynamics.RightSurface;
import org.ametiste.dynamics.Surface;
import org.ametiste.dynamics.Surge;
import org.ametiste.dynamics.foundation.reflection.structures.Annotated;
import org.ametiste.dynamics.foundation.reflection.structures.AnnotatedDescriptor;
import org.ametiste.dynamics.foundation.reflection.structures.Reference;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * The surge which is adapter between {@link org.ametiste.dynamics.foundation.reflection} and
 * {@link org.ametiste.dynamics.foundation.elements} structures.
 *
 * <p>Allows to use {@link AnnotatedRefProcessor} that defined within the <i>elements</i> scope
 * as generic {@link Surge} that is may be used within the <i>reflection</i> scope.
 *
 * @implNote From the model point of view, this surge explodes the given surface of
 * a general type in such way that the after explosion the surface becomes
 * a surface of the {@link AnnotatedRefProcessor} type.
 *
 * @apiNote At the moment each {@link AnnotatedRefProcessor} that used within the <i>reflection</i> scope
 * must be adopted using this adapter, we have hope to solve this "problem" by the {@link Planar} objects
 * that should enclose such adoptations.
 *
 * @param T a type of reference on which the exploding element holds a value
 * @param S a type of exploding element
 *
 * @see AnnotatedRefProcessor
 * @see Surge
 * @since 1.0
 */
// TODO: may we have such adoptations via planar object or not?
public class ReflectionSurfaceBinding<T, S extends Annotated & Reference<T>, C> implements Surge<S, C> {

    private final AnnotatedRefProcessor binded;

    /**
     * Creates surge that binds the given {@link AnnotatedRefProcessor} to the <i>reflection</i> surface.
     *
     * @param binded a processor to bind to <i>reflection</i> surface, must be not null
     *
     * @since 1.0
     */
    public ReflectionSurfaceBinding(final @NotNull AnnotatedRefProcessor binded) {
        this.binded = binded;
    }

    /**
     * @since 1.0
     */
    @Override
    public void explode(final @NotNull RightSurface<S> element, final @NotNull C context) {
        this.binded.explode(Surface.rightSurface(
            new AnnotatedRefFeature<T>() {
                @NotNull
                @Override
                public <T extends AnnotationSpec> T annotation(final @NotNull Function<AnnotatedDescriptor, T> spec) {
                    return element.map(f -> f.annotation(spec));
                }

                @Override
                public boolean hasAnnotations(final @NotNull Function<AnnotatedDescriptor, ? extends AnnotationSpec> spec) {
                    return element.map(f -> f.hasAnnotations(spec));
                }

                @Override
                public boolean ofType(final @NotNull Class<?> type) {
                    return element.map(f -> f.ofType(type));
                }

                @NotNull
                @Override
                public Class<T> type() {
                    return element.map(f -> f.type());
                }

                @Override
                public void referencesTo(final @NotNull T ref) {
                    element.feature(f -> f.referencesTo(ref));
                }

            }
        ), context);
    }

}
