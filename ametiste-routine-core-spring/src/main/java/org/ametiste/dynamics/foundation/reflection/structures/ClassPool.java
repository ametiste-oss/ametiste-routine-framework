package org.ametiste.dynamics.foundation.reflection.structures;

import org.ametiste.dynamics.Surface;
import org.ametiste.dynamics.SurfaceFeature;
import org.ametiste.dynamics.SurfaceStructure;
import org.ametiste.dynamics.foundation.reflection.DynamicsRuntime;
import org.ametiste.dynamics.foundation.reflection.RuntimePattern;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Represents a structure that a navigation point to all known classes of a runtime.
 * <p>The pool is formed over a pre-surface that described by a {@link DynamicsRuntime}.
 *
 * @see Surface#ofPreSurface(Supplier)
 * @see DynamicsRuntime
 * @since 1.0
 */
@SurfaceStructure(superStructure = DynamicsRuntime.class)
public final class ClassPool {

    private final DynamicsRuntime preSurface;

    public ClassPool(final @NotNull DynamicsRuntime preSurface) {
        this.preSurface = preSurface;
    }

    /**
     * Matches classes of a pool by the given matcher, maps each found class by the given mapper and
     * provides a result of mapping to the given consumer.
     *
     * <p>See {@link ClassMethod#parameters(UnaryOperator)} for matchers explanations.
     *
     * @apiNote This method represents a calculation over structure, each invocation and each consumer
     * will receive their own instance of each found {@link ClassStructure}.
     *
     * @param <T> a type of transformation applied
     * @param pattern  an operator over pattern that represents claases matcher, must be not null
     * @param mapper   a function to map found {@link ClassStructure}, must be not null
     * @param consumer a consumer of a matched fields, must be not null
     * @since 1.0
     */
    @SurfaceFeature
    public <T> void classes(final @NotNull UnaryOperator<RuntimePattern> pattern,
                            final @NotNull Function<ClassStructure, T> mapper,
                            final @NotNull Consumer<T> consumer) {
        preSurface.findClasses(pattern,
                c -> consumer.accept(mapper.apply(new ClassStructure(c)))
        );
    }

    /**
     * Matches classes of a pool by the given matcher and
     * provides a result of mapping to the given consumer as a {@link ClassStructure}.
     *
     * @apiNote Since this method represents a calculation over structure, each invocation and each consumer
     * will receive their own instance of each found {@link ClassStructure}.
     *
     * @implNote This method just uses {@link #classes(UnaryOperator, Function, Consumer)} with the empty mapper,
     * that means that the each {@link ClassStructure} will be paseed to the consumer as is.
     *
     * @param <T> a type of transformation applied
     * @param pattern  an operator over pattern that represents claases matcher, must be not null
     * @param consumer a consumer of a matched fields, must be not null
     * @since 1.0
     */
    @SurfaceFeature
    public <T> void classes(final @NotNull UnaryOperator<RuntimePattern> pattern,
                            final @NotNull Consumer<ClassStructure<?>> consumer) {
        classes(pattern, c -> c, consumer);
    }

}
