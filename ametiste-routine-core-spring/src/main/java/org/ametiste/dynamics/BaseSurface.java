package org.ametiste.dynamics;

import org.ametiste.lang.Elective;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Base implementation of a {@link Surface}.
 *
 * <p>There is nothing special about this implementation, see interface definition for details.
 *
 * @param <E> enclosing {@code Surface} type, note type of surface is depicted by it's features,
 *            so there is the type of the surface defined by a set of features
 * @param <S> {@code sub-surface} type, note type of surface is depicted by it's features,
 *            so there is the type of the surface defined by a set of features
 *
 * @apiNote This class is open to extensions, but all its methods are <i>final</i>, so
 * extensions only may add new methods and can't redefine methods behaviours.
 * <p>This feature of the class may be helpful in a situation where you need to define
 * a special kind of surface that have some additional methods.
 *
 * @see Surface
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

    @Override
    public final <T> T map(final Function<S, T> transform) {
        return transform.apply(subSurface);
    }

    @Override
    public final <T> void map(final Function<S, T> structure, final Consumer<T> features) {
        features.accept(map(structure));
    }

    @Override
    public final <T, R> R feature(final Function<S, T> structure, final Function<T, R> features) {
        return features.apply(structure.apply(subSurface));
    }

    @Override
    public final boolean isSatisfiedBy(final Predicate<S> predicate) {
        return predicate.test(subSurface);
    }

    @Override
    public final void ifSatisfied(final Predicate<S> predicate, final Consumer<S> let) {
        Elective.let(predicate, subSurface, let);
    }

    @Override
    public final void feature(final Consumer<S> feature) {
        feature.accept(subSurface);
    }

    @Override
    public final <T> Surface<S, T> depict(final Function<S, T> structure) {
        return Surface.asStructureOn(structure, subSurface);
    }

    @Override
    public final <T> void depict(final Function<S, T> structure, final Consumer<Surface<S, T>> depicted) {
        depicted.accept(depict(structure));
    }

    @Override
    public final <T extends RightSurface<S>> void depictRight(final Predicate<S> predicate,
                                                              final Function<S, T> structure,
                                                              final Consumer<T> depicted) {
        if (isSatisfiedBy(predicate)) {
            depictRight(structure, depicted);
        }
    }

    @Override
    public final <T extends RightSurface<S>> void depictRight(final Function<S, T> structure,
                                                              final Consumer<T> depicted) {
        depicted.accept(structure.apply(subSurface));
    }

}
