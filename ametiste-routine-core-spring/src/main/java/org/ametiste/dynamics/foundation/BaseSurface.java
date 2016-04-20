package org.ametiste.dynamics.foundation;

import org.ametiste.dynamics.Surface;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 *
 * @since
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

    public <F> boolean satisfiedBy(final Predicate<F> predicate, final Class<F> feature) {
        return false;
    }

    @Override
    public <T, D extends Surface<S, T>> Optional<D> depictSurface(final Function<S, T> structure) {
        return null;
    }

    @Override
    public void feature(final Consumer<S> feature) {

    }

}
