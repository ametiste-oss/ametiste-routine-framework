package org.ametiste.routine.application.service.execution;

import java.util.UUID;

/**
 *
 * @since
 */
public interface TaskExecutionService {

    void executeTask(UUID taskId);

    void completeTask(UUID taskId, String withMessage);

}
