package org.ametiste.routine.infrastructure.messaging;

import org.ametiste.routine.sdk.application.service.task.TaskAppEvenets;
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
       jmsTemplate.send("task-issued", new MessageCreator() {
           @Override
           public Message createMessage(Session session) throws JMSException {
               return session.createObjectMessage(taskId);
           }
       });
    }
}
