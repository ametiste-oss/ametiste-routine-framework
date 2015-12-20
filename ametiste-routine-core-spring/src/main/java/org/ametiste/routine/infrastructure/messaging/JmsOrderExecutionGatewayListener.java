package org.ametiste.routine.infrastructure.messaging;

import org.ametiste.routine.application.service.execution.OrderExecutionGateway;
import org.ametiste.routine.domain.task.ExecutionOrder;
import org.ametiste.routine.domain.task.TaskTerminatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;

/**
 * <p>
 *     Domain Core events listener for {@link OrderExecutionGateway},
 *     listen and convey interesting domain events to execution gateay.
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
public class JmsOrderExecutionGatewayListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final OrderExecutionGateway orderExecutionGateway;

    public JmsOrderExecutionGatewayListener(
            OrderExecutionGateway orderExecutionGateway) {
        this.orderExecutionGateway = orderExecutionGateway;
    }

    @JmsListener(destination = "task-terminated")
    public void onTaskTerminated(TaskTerminatedEvent event) throws Exception {
        orderExecutionGateway.terminateOrderExecution(event.taskId());
    }

    @JmsListener(destination = "task-prepared")
    public void onTaskPrepared(ExecutionOrder event) throws Exception {
        orderExecutionGateway.executeOrder(event);
    }

}
