package org.ametiste.routine.infrastructure.execution;

import org.ametiste.laplatform.sdk.protocol.Protocol;
import org.ametiste.laplatform.sdk.protocol.ProtocolFactory;
import org.ametiste.routine.domain.scheme.OperationScheme;
import org.ametiste.routine.domain.task.ExecutionLine;
import org.ametiste.routine.infrastructure.protocol.operation.OperationMetaProtocolRuntime;
import org.ametiste.routine.sdk.operation.OperationFeedback;

import java.util.UUID;

/**
 * <p>
 *     Operation runtime is used during operation execution runtime
 *     to create object factory that bound to the current executing operation runtime values.
 * </p>
 *
 * <p>
 *     This factory is integration point between {@code Routine Operation} runtime and external services,
 *     and should be used to create object factories that using valus from the operation runtime.
 * </p>
 *
 * @see OperationMetaProtocolRuntime
 * @since 1.1
 */
public interface OperationRuntime<T> {

    /**
     * <p>
     *    Creates object instance of the given type, that bound to concrete operation runtime.
     * </p>
     *
     * @return runtime bound instance, must be not null
     */
    // TODO: how can I register it at mod, so that I can see all runtime-bound objects
    T createRuntimeBoundObject(
            final UUID taskId,
            final ExecutionLine executionLine,
            final OperationScheme operationScheme,
            final OperationFeedback feedbackController
    );

}
