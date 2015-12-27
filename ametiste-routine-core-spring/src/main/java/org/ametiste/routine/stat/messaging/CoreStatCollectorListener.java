package org.ametiste.routine.stat.messaging;

import org.ametiste.routine.application.events.TaskDoneEvent;
import org.ametiste.routine.application.events.TaskIssuedEvent;
import org.ametiste.routine.application.events.TasksRemovedEvent;
import org.ametiste.routine.domain.task.TaskTerminatedEvent;
import org.ametiste.routine.stat.*;
import org.springframework.context.event.EventListener;

/**
 *
 * @since 0.3.0
 */
public class CoreStatCollectorListener implements
        TerminatedTasksStatCollector,
        RemovedTasksStatCollector,
        DoneTasksStatCollector,
        IssuedTasksStatCollector {

    private final CoreStatCollector coreStatCollector;

    public CoreStatCollectorListener(CoreStatCollector coreStatCollector) {
        this.coreStatCollector = coreStatCollector;
    }

    @Override
    @EventListener
    public void onTaskDone(final TaskDoneEvent event) {
        coreStatCollector.onTaskDone(event);
    }

    @Override
    @EventListener
    public void onTaskIssued(final TaskIssuedEvent event) {
        coreStatCollector.onTaskIssued(event);
    }

    @Override
    @EventListener
    public void onTasksRemoved(final TasksRemovedEvent event) {
        coreStatCollector.onTasksRemoved(event);
    }

    @Override
    @EventListener
    public void onTaskTerminated(final TaskTerminatedEvent event) {
        coreStatCollector.onTaskTerminated(event);
    }
}
