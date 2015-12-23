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

    void removeTasks(List<Task.State> states, Instant after);

}
