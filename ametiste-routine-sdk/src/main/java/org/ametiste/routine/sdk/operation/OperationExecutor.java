package org.ametiste.routine.sdk.operation;

import org.ametiste.laplatform.protocol.ProtocolGateway;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @since
 */
public interface OperationExecutor {

    // TODO: remove default after deprection complete
    // TODO: fold operationId and properties to some value object
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
