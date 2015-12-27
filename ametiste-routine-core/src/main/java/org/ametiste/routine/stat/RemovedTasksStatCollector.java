package org.ametiste.routine.stat;

import org.ametiste.routine.application.events.TasksRemovedEvent;
import org.ametiste.routine.domain.task.TaskTerminatedEvent;

/**
 *
 * @since 0.3.0
 */
public interface RemovedTasksStatCollector {

    void onTasksRemoved(TasksRemovedEvent event);

}
