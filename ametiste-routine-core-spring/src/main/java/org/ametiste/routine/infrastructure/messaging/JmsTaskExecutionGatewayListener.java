package org.ametiste.routine.infrastructure.messaging;

import org.ametiste.routine.infrastructure.execution.TaskExecutionGateway;
import org.ametiste.routine.domain.task.TaskTerminatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;

import java.util.UUID;

/**
 * <p>
 *     Domain Core events listener for {@link TaskExecutionGateway},
 *     listen and convey interesting domain events to termination gateay.
 * </p>
 *
 * <p>
 *     Note, intensity of messages consuming is controled by order gateay implementation,
 *     usually if the gateway can't consume more messages, it should block next
 *     message consuming method invokation.
 * </p>
 *
 * @since 0.1.0
 */
public class JmsTaskExecutionGatewayListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final TaskExecutionGateway taskExecutionGateway;

    public JmsTaskExecutionGatewayListener(TaskExecutionGateway taskExecutionGateway) {
        this.taskExecutionGateway = taskExecutionGateway;
    }

    @JmsListener(destination = "task-terminated")
    public void onTaskTerminated(TaskTerminatedEvent event) throws Exception {
        taskExecutionGateway
                .terminateTaskExecution(event.taskId());
    }

    @JmsListener(destination = "task-issued")
    public void onTaskPrepared(UUID taskId) throws Exception {
        taskExecutionGateway.startTaskExecution(taskId);
    }

}
