package org.ametiste.routine.dsl.application;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.dsl.annotations.OperationParameter;
import org.ametiste.routine.meta.util.MetaMethodParameter;

import java.util.Optional;

/**
 * A provider interface for <i>dsl operation</i> parameters providing. This is the
 * extension point for operation parameters resolving feature.
 * <p>
 * Implementations allow to define various mechanism to resolve operation parameters at runtime. For
 * example, {@link org.ametiste.routine.dsl.configuration.task.params.OperationParameterProvider} implements
 * parameters resolving using {@link OperationParameter} parameter annotation.
 *
 * @see org.ametiste.routine.dsl.configuration.task.params.OperationParameterProvider
 * @since 1.1
 */
public interface ParameterProvider {

    Optional<Object> provideValue(MetaMethodParameter parameter, ProtocolGateway protocolGateway);
    
}
