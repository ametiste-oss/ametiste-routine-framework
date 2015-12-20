package org.ametiste.routine.application.service.termination;

import java.util.UUID;

/**
 * <p>
 *     Just terminates tasks, brutally and rigor.
 * </p>
 *
 * @since 0.1.0
 */
public interface TaskTerminationService {

    void terminateTask(UUID taskId, String withMessage);

}
