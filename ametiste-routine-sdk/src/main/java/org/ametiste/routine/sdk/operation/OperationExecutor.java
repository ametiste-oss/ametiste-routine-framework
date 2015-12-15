package org.ametiste.routine.sdk.operation;

import org.ametiste.routine.sdk.mod.protocol.ProtocolGateway;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @since
 */
public interface OperationExecutor {

    // TODO remove default after deprection complete
    default void execOperation(UUID operationId,
                       Map<String, String> properties,
                       OperationFeedback feedback,
                       ProtocolGateway protocolGateway) {
        this.execOperation(operationId, properties, feedback);
    };

    @Deprecated
    default void execOperation(
            UUID operationId,
            Map<String, String> properties,
            OperationFeedback feedback
    ) {};

}
