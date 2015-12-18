package org.ametiste.routine.infrastructure.messaging;

import org.ametiste.routine.application.service.TaskDomainEvenets;
import org.ametiste.routine.domain.task.ExecutionOrder;
import org.ametiste.routine.domain.task.OperationTerminatedEvent;
import org.ametiste.routine.domain.task.TaskTerminatedEvent;
import org.springframework.jms.core.JmsTemplate;

import java.util.UUID;

/**
 *
 * @since
 */
public class JmsTaskDomainEvents implements TaskDomainEvenets {

    private final JmsTemplate jmsTemplate;

    public JmsTaskDomainEvents(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void taskIssued(final UUID taskId) {
       jmsTemplate.send("task-issued", (s) -> {
               return s.createObjectMessage(taskId);
           }
       );
    }

    @Override
    public void taskPended(ExecutionOrder executionOrder) {
        jmsTemplate.send("task-pended", (s) -> {
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
