package org.ametiste.routine.application.service.execution;

import java.util.UUID;

/**
 *
 * @since
 */
public interface TaskExecutionService {

    void pendTaskForExecution(UUID taskId);

    void terminateTask(UUID taskId, String withMessage);

}
