package org.ametiste.routine.infrastructure.messaging;

import org.ametiste.routine.application.service.TaskDomainEvenetsGateway;
import org.ametiste.routine.domain.task.ExecutionOrder;
import org.ametiste.routine.domain.task.OperationTerminatedEvent;
import org.ametiste.routine.domain.task.TaskTerminatedEvent;
import org.springframework.jms.core.JmsTemplate;

import java.util.UUID;

/**
 *
 * @since
 */
public class JmsTaskDomainEventsGateway implements TaskDomainEvenetsGateway {

    private final JmsTemplate jmsTemplate;

    public JmsTaskDomainEventsGateway(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void taskIssued(final UUID taskId) {
        jmsTemplate.send("task-issued", s -> s.createObjectMessage(taskId));
    }

    @Override
    public void operationTerminated(OperationTerminatedEvent event) {
        // NOTE: atm there is no listener for this event, so is not required to be sent
        // jmsTemplate.send("task-op-terminated", s -> s.createObjectMessage(event));
    }

    @Override
    public void taskTerminated(TaskTerminatedEvent event) {
        jmsTemplate.send("task-terminated", s -> s.createObjectMessage(event));
    }

}
