package org.ametiste.dynamics;

import org.ametiste.dynamics.foundation.reflection.DynamicsRuntime;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Surface of a runtime object subgraph, represents any runtime subgraph in terms of
 * the surface model.
 * <p>
 * <p>
 * See <a href="https://ametiste.atlassian.net/wiki/display/AMEPUB/AME+Dynamics+%3A+Surface+Reflection+Model#AMEDynamics:SurfaceReflectionModel-Surfaces">Ame Dynamics : Surfaces</a>
 * for formal difinition of a surface.
 * </p>
 *
 * @param <E> enclosing {@code Surface} type, note type of surface is depicted by it's features,
 *            so there is the type of the surface defined by a set of features
 * @param <S> {@code sub-surface} type, note type of surface is depicted by it's features,
 *            so there is the type of the surface defined by a set of features
 * @see <a href="https://ametiste.atlassian.net/wiki/display/AMEPUB/AME+Dynamics+%3A+Surface+Reflection+Model#AMEDynamics:SurfaceReflectionModel-Surfaces">Ame Dynamics : Surfaces</a>
 * @see Surge
 * @see RightSurface
 * @see LeftSurface
 * @since 1.1
 */
public interface Surface<E, S> {

    /**
     * Checks that a sub-surface structure is satisfied by the given predicate.
     *
     * @param predicate a predicate to test sub-surface structure, must be not null
     * @return {@code true} if the given predicate is true for this surface, {@code false} otherwise.
     * @since 1.0
     */
    boolean isSatisfiedBy(Predicate<S> predicate);

    /**
     * Passes the sub-surface structure feature to the given consumer, if the given preducate
     * is true over this sub-surface structure.
     *
     * @param predicate a predicate to test sub-surface structure, must be not null
     * @param feature a feature consumer, must be not null
     * @since 1.0
     */
    void ifSatisfied(Predicate<S> predicate, Consumer<S> feature);

    /**
     * Transforms a sub-surface of this {@code surface} by the given transformation as {@code structure}
     * and returns the result of this transformation.
     *
     * @param structure a function to extract a structure of this {@code surface}, must be not null
     * @param <T>       a type of {@code structure} that would be extracted from this surface
     * @return result of the {@code structure} transformation, may be null
     * @apiNote Also this method is may be refered as <i>rightMap</i>. Sub-surface is 'right' part of a surface,
     * so this method maps right part.
     * @since 1.0
     */
    <T> T map(Function<S, T> structure);

    /**
     * Transforms a sub-surface of this {@code surface} by the given transformation as {@code structure}
     * and pass the obtained {@code structure} to the given {@code feature} consumer.
     * <p>
     * <pre>
     * pool.map(e -> new RoutineDSLStructure(e, fields, params), dsl ->
     *      dsl.tasks(task ->
     *          schemeRepository.saveScheme(
     *              new DynamicTaskScheme(
     *                  task.name(),
     *                  task.operations(DynamicOperationScheme::new)
     *                      .peek(schemeRepository::saveScheme)
     *                      .collect(Collectors.toList())
     *              )
     *          )
     *      )
     * )
     * </pre>

     * @param structure a function to extract a structure of this {@code surface}, must be not null
     * @param feature  a feature consumer, must be not null
     * @param <T>       a type of {@code structure} that would be extracted from this surface
     * @param <R>       a type of result that would be optined by application of the {@code feature} function
     * @return result of the {@code feature} function application over extracted {@code structure}, may be null
     * @apiNote Also this method is may be refered as <i>rightMap</i>. Sub-surface is 'right' part of a surface,
     * so this method maps right part.
     * @since 1.0
     */
    <T> void map(Function<S, T> structure, Consumer<T> feature);

    /**
     * Passes the sub-surface structure feature to the given consumer.
     *
     * @param feature a feature consumer, must be not null
     * @since 1.0
     */
    void feature(Consumer<S> feature);

    /**
     * Transfor a sub-surface of this {@code surface} by the given transformation as {@code structure}
     * and pass the obtained {@code structure} to the given {@code feature} function. Retunrs result
     * of the {@code feature} function application. For example
     *
     * @param structure a function to extract a structure of this {@code surface}, must be not null
     * @param feature   a function to be applied on the extracted {@code structure}, must be not null
     * @param <T>       a type of {@code structure} that would be extracted from this surface
     * @param <R>       a type of result that would be optined by application of the {@code feature} function
     * @return result of the {@code feature} function application over extracted {@code structure}, may be null
     * @apiNote This method have to use in a situation where some transformation over a sub-surfac is required,
     * and the result of this transformation is required by externals.
     * @since 1.0
     */
    <T, R> R feature(Function<S, T> structure, Function<T, R> feature);

    /**
     * Transforms a sub-surface of this {@code surface} by the given transformation as {@code structure}
     * and returns the result of this transformation to the given consumer as new {@link Surface} enclosed
     * by this surface and that has sub-surface depicted by the {@code structure}.
     *
     * @param structure a function to extract a structure of this {@code surface}, must be not null
     * @param <T>       a type of {@code structure} that would be extracted from this surface
     * @return a surface enclosed by this surface and depicted by the {@code structure} transformation, may be null
     * @since 1.0
     */
    <T> Surface<S, T> depict(Function<S, T> structure);

    /**
     * Transforms a sub-surface of this {@code surface} by the given transformation as {@code structure}
     * and pass the result of this transformation to the given consumer as new {@link Surface} enclosed
     * by this surface and that has sub-surface depicted by the {@code structure}.
     *
     * @param structure a function to extract a structure of this {@code surface}, must be not null
     * @param depicted  a surface consumer, must be not null
     * @param <T>       a type of {@code structure} that would be extracted from this surface
     * @since 1.0
     */
    <T> void depict(Function<S, T> structure, Consumer<Surface<S, T>> depicted);

    /**
     * This method is same to {@link #depict(Function, Consumer)}, but results in {@link RightSurface}.
     *
     * @param structure a function to extract a structure of this {@code surface}, must be not null
     * @param depicted  a surface consumer, must be not null
     * @param <T> an inference type of a resulting {@code RightSurface}, binding strcutre
     *            type and consumer type together
     * @see #depict(Function, Consumer)
     * @see RightSurface
     * @since 1.0
     */
    <T extends RightSurface<S>> void depictRight(Function<S, T> structure, Consumer<T> depicted);

    /**
     * This method is same to {@link #depict(Function, Consumer)},
     * but results in {@link RightSurface} only if the given {@code predicate} is ture
     * for this surface's sub-surface.
     *
     * @param predicate a predicate to test sub-surface structure, must be not null
     * @param structure a function to extract a structure of this {@code surface}, must be not null
     * @param depicted  a surface consumer, must be not null
     * @param <T> an inference type of a resulting {@code RightSurface}, binding strcutre
     *            type and consumer type together
     * @see #depict(Function, Consumer)
     * @see RightSurface
     * @since 1.0
     */
    <T extends RightSurface<S>> void depictRight(Predicate<S> predicate, Function<S, T> structure, Consumer<T> depicted);

    /**
     * Builds a surface from the given pre-surface, the surface
     * such that built over the {@link Void} surface.
     * <p>
     * <p>The pre-surface is a base set of features to build any surface from the {@code Void}. See
     * {@link DynamicsRuntime} for example of a pre-surface.
     * <p>
     * <p>From a technical point of view, a pre-surface usually will be
     * defined as interface, so it may act like an adapter object for a source
     * object of various kind to build a surface over it.
     *
     * @param preSurface a pre-surface supplier to build a surface over it, must be not null
     * @param <S>        a type of pre-surface, defines presurface structure
     * @return a surface built over the given pre-surface, can't be null
     * @since 1.0
     */
    static <S> Surface<Void, S> ofPreSurface(final Supplier<S> preSurface) {
        return new BaseSurface<Void, S>(preSurface);
    }

    /**
     * Shortcuts a pre-surface creation in case where pre-surface is exists.
     * See {@link #ofPreSurface(Supplier)} for details.
     *
     * @param preSurface a pre-surface to build a surface over it, must be not null
     * @param <S> the presurface object to build a surface over it, must be not null
     * @return a surface built over the given pre-surface, can't be null
     * @see #ofPreSurface(Supplier)
     * @since 1.0
     */
    static <S> Surface<Void, S> ofPreSurface(final S preSurface) {
        return new BaseSurface<Void, S>(() -> preSurface);
    }

    /**
     * Creates {@link RightSurface} depicted by the given feature.
     *
     * @param feature a feature to depict new surface sub-surface, must be not null
     * @param <S> a feature to depict new surface
     * @return a surface built over the given pre-surface, can't be null
     * @see RightSurface
     * @since 1.0
     */
    static <S> RightSurface<S> rightSurface(final S feature) {
        return new BaseRightSurface<S>(() -> feature);
    }

    /**
     * Depicts a surface of the given {@code structure} on the given features set.
     * <p>
     * <p>Note, any set of features forms a surface, so in other words, this method depicts
     * a features-surface of a surface defined by the given features set.
     *
     * @param structure function to form structure of new surface
     * @param feature   set of features upon which the surface will be built
     * @param <S>       type of features set
     * @param <T>       type of depicting surface
     * @return new surface formed by the given structure on the given features set
     * @since 1.0
     */
    static <S, T> Surface<S, T> asStructureOn(final Function<S, T> structure, final S feature) {
        return new BaseSurface<>(feature, structure);
    }

}
