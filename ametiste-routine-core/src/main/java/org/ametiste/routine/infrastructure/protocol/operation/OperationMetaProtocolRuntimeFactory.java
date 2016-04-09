package org.ametiste.routine.infrastructure.protocol.operation;

import org.ametiste.laplatform.sdk.protocol.ProtocolFactory;
import org.ametiste.routine.domain.scheme.OperationScheme;
import org.ametiste.routine.domain.task.ExecutionLine;
import org.ametiste.routine.infrastructure.execution.OperationRuntimeProtocolFactory;
import org.ametiste.routine.sdk.operation.OperationFeedback;
import org.ametiste.routine.sdk.protocol.operation.OperationMetaProtocol;

import java.util.UUID;

/**
 * <p>
 *     Provides access to operation runtime metadata.
 * </p>
 *
 * @since 1.1
 */
public class OperationMetaProtocolRuntimeFactory implements OperationRuntimeProtocolFactory<OperationMetaProtocol> {

    // TODO: how can I register it at mod, so that I can see this factory/protocol via http interface
    // TODO: how can I describe its @ProtocolMeta paramteres? So that I can have auto-metrics for such protocols
    @Override
    public ProtocolFactory<OperationMetaProtocol> runtimeProtocolFactory(final ExecutionLine executionLine,
                                                                         final OperationScheme operationScheme,
                                                                         final OperationFeedback feedbackController) {
        return (gw) -> new OperationMetaProtocol() {

            @Override
            public UUID operationId() {
                return executionLine.operationId();
            }

            @Override
            public String operationName() {
                return executionLine.operationName();
            }

        };
    }

}
