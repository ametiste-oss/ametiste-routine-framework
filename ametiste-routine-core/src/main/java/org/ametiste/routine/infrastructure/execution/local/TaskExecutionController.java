package org.ametiste.routine.infrastructure.execution.local;

import org.ametiste.routine.domain.task.ExecutionOrder;

import java.util.UUID;

/**
 *
 * @since
 */
public interface TaskExecutionController {

    ExecutionOrder startTaskExecution(UUID taskId);

    void operationStarted(UUID operationId);

    void operationDone(UUID operationId);

    void operationStarted(UUID operationId, String withMessage);

    void operationDone(UUID operationId, String withMessage);

    void operationNotice(UUID operationId, String noticeMessage);

    void operationFailed(UUID operationId, String withMessage);

}
