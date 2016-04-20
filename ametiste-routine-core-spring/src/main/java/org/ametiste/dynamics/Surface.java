package org.ametiste.dynamics;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Surface of a runtime object subgraph, represents any runtime subgraph in terms of
 * the surface model.
 *
 * <p>
 *     See <a href="https://ametiste.atlassian.net/wiki/display/AMEPUB/AME+Dynamics+%3A+Surface+Reflection+Model#AMEDynamics:SurfaceReflectionModel-Surfaces">Ame Dynamics : Surfaces</a>
 *     for formal difinition of a surface.
 * </p>
 *
 * @since 1.1
 *
 * @param E enclosing {@code Surface} type, note type of surface is depicted by it's feature,
 *          so there is set of features
 * @param S {@code sub-surface} type, note type of surface is depicted by it's feature,
 *          so there is set of features
 * @see Surface
 * @see DynamicSurfaceStructure
 * @see SurfaceElement
 * @see <a href="https://ametiste.atlassian.net/wiki/display/AMEPUB/AME+Dynamics+%3A+Surface+Reflection+Model#AMEDynamics:SurfaceReflectionModel-Surfaces">Ame Dynamics : Surfaces</a>
 */
public interface Surface<E, S> {

//    <F, T> Optional<T> map(Function<F, T> transformFeature, Class<F> feature);
//
//    <F> boolean satisfiedBy(Predicate<F> predicate, Class<F> feature);
//
    void feature(Consumer<S> feature);


    //  Тут должна быть не совсем функция, а объект заключающий предикат проверки и функцию,
    // тогда я смогу строить RoutineDSLSurface из ClassPoolSurface
    <T, D extends Surface<S, T>> Optional<D> depictSurface(Function<S, T> structure);

    <T> Optional<T> depictFeatures(Function<S, T> structure);

}
