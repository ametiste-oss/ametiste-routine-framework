package org.ametiste.routine.infrastructure.messaging;

import org.ametiste.routine.application.service.TaskAppEvenets;
import org.ametiste.routine.domain.task.ExecutionOrder;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.UUID;

/**
 *
 * @since
 */
public class JmsTaskAppEvents implements TaskAppEvenets {

    private final JmsTemplate jmsTemplate;

    public JmsTaskAppEvents(JmsTemplate jmsTemplate) {
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
}
