package org.ametiste.routine.sdk.operation;

import org.ametiste.laplatform.protocol.ProtocolGateway;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @since
 */
public interface OperationExecutor {

    void execOperation(OperationFeedback feedback, ProtocolGateway protocolGateway);

}
