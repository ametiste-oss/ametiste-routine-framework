package org.ametiste.dynamics.foundation;

import org.ametiste.dynamics.Surface;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 *
 * @since 1.0
 */
public class BaseSurface<E, S> implements Surface<E, S> {

    protected final E enclosingSurface;
    protected final S subSurface;

    public BaseSurface(final E enclosingSurface, final Function<E, S> subSurface) {
        this.enclosingSurface = enclosingSurface;
        this.subSurface = subSurface.apply(enclosingSurface);
    }

    public BaseSurface(final Supplier<S> subSurface) {
        this(null, e -> subSurface.get());
    }

    public BaseSurface(final E enclosingSurface) {
        this(enclosingSurface, null);
    }

    public boolean satisfiedBy(final Predicate<S> predicate) {
        return predicate.test(subSurface);
    }

    @Override
    public <T> Surface<S, T> depictSurface(final Function<S, T> structure) {
        return Surface.asStructureOn(structure, subSurface);
    }

    @Override
    public <T> void depictSurface(final Function<S, T> structure, final Consumer<Surface<S, T>> depicted) {
        depicted.accept(depictSurface(structure));
    }

    @Override
    public <T> T depictFeatures(final Function<S, T> structure) {
        return structure.apply(subSurface);
    }

    @Override
    public <T> void depictFeatures(final Function<S, T> structure, final Consumer<T> features) {
        features.accept(depictFeatures(structure));
    }

    @Override
    public void feature(final Consumer<S> feature) {
        feature.accept(subSurface);
    }

}
