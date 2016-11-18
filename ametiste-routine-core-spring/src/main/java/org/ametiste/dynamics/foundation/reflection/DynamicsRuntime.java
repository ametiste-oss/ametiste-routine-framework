package org.ametiste.dynamics.foundation.reflection;

import org.ametiste.dynamics.PreSurface;
import org.ametiste.dynamics.Surface;
import org.ametiste.dynamics.foundation.reflection.structures.ClassMethod;
import org.ametiste.dynamics.foundation.reflection.structures.ClassStructure;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * A repostitory over a runtime objects graph.
 * <p>Provides convenient methods to access an object of runtime.
 *
 * @apiNote From the point of view of the Surface Model this interface forms <i>pre-surface</i> necessary
 * to build {@link Surface} over a runtime objects graph.
 *
 * @see PreSurface
 * @since 1.0
 */
@PreSurface
public interface DynamicsRuntime {

    /**
     * Matches classes of a runtime by the given matcher, provides each matched class to the given consumer.
     *
     * <p>See {@link ClassMethod#parameters(UnaryOperator)} for matchers explanations.
     *
     * @implSpec This method represents a calculation over a structure, each invocation and each consumer
     * will receive their own instance of each found {@link Class} instance.
     *
     * @param matcher  an operator over pattern that represents claases matcher, must be not null
     * @param consumer a consumer of a matched classes, must be not null
     *
     * @see UnaryOperator
     * @see RuntimePattern
     * @see Class
     * @since 1.0
     */
    void findClasses(@NotNull UnaryOperator<RuntimePattern> matcher, @NotNull Consumer<Class<?>> consumer);

}
