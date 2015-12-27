package org.ametiste.routine.stat;

import org.ametiste.routine.application.events.TaskDoneEvent;

/**
 *
 * @since 0.3.0
 */
public interface DoneTasksStatCollector {

    void onTaskDone(TaskDoneEvent event);

}
