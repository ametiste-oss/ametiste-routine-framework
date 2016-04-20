package org.ametiste.dynamics;

import org.ametiste.routine.dsl.annotations.OperationParameter;

import java.util.Optional;

/**
 *
 * Provides some value based on a target {@link SurfaceElement} description.
 *
 * <p>
 *     {@link SurfaceElement} is an abstraction that used to define "runtime elements" - any part
 *     of operational.
 *
 * <p>
 *     Implementations allow to define various mechanism to resolve elements values at a runtime. For
 *     example, {@link org.ametiste.routine.dsl.configuration.task.params.OperationParameterProvider} implements
 *     parameters resolving using {@link OperationParameter} parameter annotation.
 *
 * @param <C> defines type of context that required by the provier implementation
 * @see SurfaceElement
 * @see org.ametiste.routine.dsl.configuration.task.params.OperationParameterProvider
 * @since 1.1
 */
public interface DynamicValueProvider<C> {

    Optional<Object> provideValue(SurfaceElement element, C protocolGateway);
    
}
