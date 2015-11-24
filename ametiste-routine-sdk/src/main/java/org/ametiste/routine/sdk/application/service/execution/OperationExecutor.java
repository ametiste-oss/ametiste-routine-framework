package org.ametiste.routine.sdk.application.service.execution;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @since
 */
public interface OperationExecutor {

    void execOperation(
            UUID operationId,
            Map<String, String> properties,
            OperationFeedback feedback
    );

}
