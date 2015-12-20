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
       /*

           TODO: note, atm doing nothing, unused event that just blowing the broker

           jmsTemplate.send("task-issued", (s) -> {
               return s.createObjectMessage(taskId);
           }
       ); */
    }

    @Override
    public void taskExecutionPrepared(ExecutionOrder executionOrder) {
        jmsTemplate.send("task-prepared", (s) -> {
            return s.createObjectMessage(executionOrder);
        });
    }

    @Override
    public void operationTerminated(OperationTerminatedEvent event) {
        jmsTemplate.send("task-op-terminated", (s) -> {
            return s.createObjectMessage(event);
        });
    }

    @Override
    public void taskTerminated(TaskTerminatedEvent event) {
        jmsTemplate.send("task-terminated", (s) -> {
            return s.createObjectMessage(event);
        });
    }
}
