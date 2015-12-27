package org.ametiste.routine.application;

import org.ametiste.routine.application.events.TaskDoneEvent;
import org.ametiste.routine.application.events.TasksRemovedEvent;
import org.ametiste.routine.application.events.TaskIssuedEvent;
import org.ametiste.routine.domain.task.TaskTerminatedEvent;

/**
 *
 * @since
 */
public interface CoreEventsGateway {

    void taskIssued(TaskIssuedEvent taskIssuedEvent);

    void taskTerminated(TaskTerminatedEvent event);

    void tasksRemoved(TasksRemovedEvent tasksRemovedEvent);

    void taskDone(TaskDoneEvent taskDoneEvent);

}
