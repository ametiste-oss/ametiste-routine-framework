package org.ametiste.routine.stat;

import org.ametiste.routine.domain.task.TaskTerminatedEvent;

/**
 *
 * @since
 */
public interface TerminatedTasksStatCollector {

    void onTaskTerminated(TaskTerminatedEvent event);

}
