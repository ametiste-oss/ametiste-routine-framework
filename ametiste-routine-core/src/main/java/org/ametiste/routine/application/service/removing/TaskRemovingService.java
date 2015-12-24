package org.ametiste.routine.application.service.removing;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.sdk.domain.TaskFilter;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 * @since
 */
public interface TaskRemovingService {

    /**
     * <p>
     *     Removes completed tasks in the given states which completion time is greater
     *     than the given value.
     * </p>
     *
     * <p>
     *     Note, only tasks that in one of {@link Task.State#completeState} may be removed
     *     using this service opertion.
     * </p>
     *
     * @param states list of states, tasks in which must be removed
     * @param after completion date threshold
     */
    void removeTasks(List<Task.State> states, Instant after);

}
