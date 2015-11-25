package org.ametiste.routine.sdk.application.service.execution;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @since
 */
public interface OperationExecutionGateway {

    void executeOperation(
            UUID operationId,
            String operationExecLine,
            Map<String, String> properties,
            ExecutionFeedback feedback);

}