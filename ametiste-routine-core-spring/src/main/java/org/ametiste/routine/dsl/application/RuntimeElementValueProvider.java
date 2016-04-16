package org.ametiste.routine.dsl.application;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.dsl.annotations.OperationParameter;

import java.util.Optional;

/**
 *
 * Provides some value based on a target {@link RuntimeElement} description.
 *
 * <p>
 *     {@link RuntimeElement} is an abstraction that used to define "runtime elements" - any part
 *     of operational.
 *
 * <p>
 *     Implementations allow to define various mechanism to resolve elements values at a runtime. For
 *     example, {@link org.ametiste.routine.dsl.configuration.task.params.OperationParameterProvider} implements
 *     parameters resolving using {@link OperationParameter} parameter annotation.
 *
 * @see RuntimeElement
 * @see org.ametiste.routine.dsl.configuration.task.params.OperationParameterProvider
 * @since 1.1
 */
public interface RuntimeElementValueProvider {

    Optional<Object> provideValue(RuntimeElement element, ProtocolGateway protocolGateway);
    
}
