package org.ametiste.routine.infrastructure.messaging;

import org.ametiste.routine.application.CoreEventsGateway;
import org.ametiste.routine.application.events.TaskDoneEvent;
import org.ametiste.routine.application.events.TaskIssuedEvent;
import org.ametiste.routine.application.events.TasksRemovedEvent;
import org.ametiste.routine.domain.task.TaskTerminatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

/**
 * <p>
 *     Simple {@link CoreEventsGateway} that use {@link ApplicationEventPublisher}
 *     for events broadcasting across the application.
 * </p>
 *
 * @since 0.3.0
 */
public class SpringCoreEventsGateway implements CoreEventsGateway {

    private final ApplicationEventPublisher eventPublisher;

    public SpringCoreEventsGateway(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void taskIssued(final TaskIssuedEvent taskIssuedEvent) {
        eventPublisher.publishEvent(taskIssuedEvent);
    }

    @Override
    public void taskTerminated(final TaskTerminatedEvent event) {
        eventPublisher.publishEvent(event);
    }

    @Override
    public void tasksRemoved(final TasksRemovedEvent event) {
        eventPublisher.publishEvent(event);
    }

    @Override
    public void taskDone(final TaskDoneEvent event) {
        eventPublisher.publishEvent(event);
    }
}
