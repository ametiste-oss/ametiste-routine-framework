package org.ametiste.dynamics;

import org.ametiste.dynamics.foundation.BaseSurface;
import org.ametiste.dynamics.foundation.structure.ReflectFoundation.RuntimePreSurface;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Surface of a runtime object subgraph, represents any runtime subgraph in terms of
 * the surface model.
 *
 * <p>
 *     See <a href="https://ametiste.atlassian.net/wiki/display/AMEPUB/AME+Dynamics+%3A+Surface+Reflection+Model#AMEDynamics:SurfaceReflectionModel-Surfaces">Ame Dynamics : Surfaces</a>
 *     for formal difinition of a surface.
 * </p>
 *
 * @param <E> enclosing {@code Surface} type, note type of surface is depicted by it's features,
 *           so there is the type of the surface defined by a set of features
 * @param <S> {@code sub-surface} type, note type of surface is depicted by it's features,
 *           so there is the type of the surface defined by a set of features
 *
 * @see Surface
 * @see DynamicSurfaceStructure
 * @see SurfaceElement
 * @see <a href="https://ametiste.atlassian.net/wiki/display/AMEPUB/AME+Dynamics+%3A+Surface+Reflection+Model#AMEDynamics:SurfaceReflectionModel-Surfaces">Ame Dynamics : Surfaces</a>
 * @since 1.1
 */
public interface Surface<E, S> {

//    <F, T> Optional<T> map(Function<F, T> transformFeature, Class<F> feature);
//
//    <F> boolean satisfiedBy(Predicate<F> predicate, Class<F> feature);
//
    void feature(Consumer<S> feature);

    //  Тут должна быть не совсем функция, а объект заключающий предикат проверки и функцию,
    // тогда я смогу строить RoutineDSLSurface из ClassPoolSurface
    <T> Surface<S, T> depictSurface(Function<S, T> structure);

    <T> void depictSurface(Function<S, T> structure, Consumer<Surface<S, T>> depicted);

    <T> T depictFeatures(Function<S, T> structure);

    <T> void depictFeatures(Function<S, T> structure, Consumer<T> features);

    /**
     * Builds a surface from the given pre-surface, the surface
     * such that built over the {@link Void} surface.
     *
     * <p>The pre-surface is a base to build any surface from the Void. See
     * {@link RuntimePreSurface} for example of a pre-surface.
     *
     * <p>From a technical point of view, a pre-surface usually will be
     * defined as interface, so it may act like an adapter object for a source
     * object of various kind to build a surface over it.
     *
     * @param preSurface the pre-surface supplier to built a surface over it, must be not null
     * @param <S> type of pre-surface, defines presurface structure
     *
     * @return a surface built over the given pre-surface, can't be null
     */
    static <S> Surface<Void, S> byPreSurface(Supplier<S> preSurface) {
        return new BaseSurface<Void, S>(preSurface);
    }

    /**
     * Depicts a surface of the given {@code structure} on the given features set.
     *
     * <p>Note, any set of features forms a surface, so in other words, this method depicts
     * a features-surface of a surface defined by the given features set.
     *
     * @param structure function to form structure of new surface
     * @param features set of features upon which the surface will be built
     * @param <S> type of features set
     * @param <T> type of depicting surface
     *
     * @return new surface formed by the given structure on the given features set
     */
    static <S, T> Surface<S, T> asStructureOn(final Function<S, T> structure, final S features) {
        return new BaseSurface<>(features, structure);
    }

}
