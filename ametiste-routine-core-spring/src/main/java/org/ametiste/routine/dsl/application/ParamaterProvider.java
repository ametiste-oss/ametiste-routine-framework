package org.ametiste.routine.dsl.application;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.dsl.annotations.OperationParameter;
import org.ametiste.routine.meta.util.MetaMethodParameter;

import java.util.Optional;

/**
 * <p>
 *     A provider interface for <i>dsl operation</i> paramteres providing. This is the
 *     extension point for operation parameters resolvin feature.
 * </p>
 *
 * <p>
 *     Implementations allow to define various mechanism to resolve operation paramteres at runtime. For
 *     example, {@link org.ametiste.routine.dsl.configuration.task.params.OperationParameterProvider} implements
 *     parameteres resolving using {@link OperationParameter} paramter annotation.
 * </p>
 *
 * @since 1.1
 * @see org.ametiste.routine.dsl.configuration.task.params.OperationParameterProvider
 */
public interface ParamaterProvider {

    Optional<Object> provideValue(MetaMethodParameter parameter, ProtocolGateway protocolGateway);
    
}
