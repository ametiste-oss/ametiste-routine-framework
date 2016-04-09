package org.ametiste.routine.infrastructure.execution;

import org.ametiste.laplatform.sdk.protocol.Protocol;
import org.ametiste.laplatform.sdk.protocol.ProtocolFactory;
import org.ametiste.routine.domain.scheme.OperationScheme;
import org.ametiste.routine.domain.task.ExecutionLine;
import org.ametiste.routine.sdk.operation.OperationFeedback;

/**
 *
 * @since
 */
public interface OperationRuntimeProtocolFactory<T extends Protocol> {

    /**
     * <p>
     *      Creates {@link ProtocolFactory} instance, that bound to concrete operation runtime.
     * </p>
     *
     * @return protocol factory instance, must be not null
     */
    ProtocolFactory<T> runtimeProtocolFactory(
            final ExecutionLine executionLine,
            final OperationScheme operationScheme,
            final OperationFeedback feedbackController
    );

}
