package org.ametiste.routine.application.service.execution;

import java.util.UUID;

/**
 *
 * @since
 */
public interface TaskTerminationService {

    void terminateTask(UUID taskId, String withMessage);

}
