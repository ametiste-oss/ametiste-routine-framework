package org.ametiste.routine.stat;

import org.ametiste.routine.application.events.TaskIssuedEvent;

/**
 *
 * @since 0.3.0
 */
public interface IssuedTasksStatCollector {

    void onTaskIssued(TaskIssuedEvent event);

}
