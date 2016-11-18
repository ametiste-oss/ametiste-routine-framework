package org.ametiste.dynamics.foundation.spring;

import org.ametiste.dynamics.foundation.reflection.DynamicsRuntime;
import org.ametiste.dynamics.foundation.reflection.RuntimePattern;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * A repostitory over a Spring {@link ApplicationContext} that is used as
 * underlying runtime objects graph.
 * <p>Provides access to objects defined within the context.
 *
 * @see DynamicsRuntime
 * @since 1.0
 */
public class SpringAppContextDynamicsRuntime implements DynamicsRuntime {

    private final ApplicationContext appContext;

    /**
     * Crates new runtime using the given {@link ApplicationContext} as an underlying runtime objects graph.
     *
     * @param appContext a application context, must be not null
     * @since 1.0
     */
    public SpringAppContextDynamicsRuntime(final @NotNull ApplicationContext appContext) {
        this.appContext = appContext;
    }

    /**
     * @since 1.0
     */
    @Override
    public void findClasses(final @NotNull UnaryOperator<RuntimePattern> matcher,
                            final @NotNull Consumer<Class<?>> consumer) {
        RuntimePattern.create(matcher).let(annotations ->
            appContext.getBeansWithAnnotation(annotations.get(0)).values().stream()
                    .filter(o -> annotations.stream().allMatch(o.getClass()::isAnnotationPresent))
                    .map(Object::getClass)
                    .forEach(consumer)
        );
    }

}
