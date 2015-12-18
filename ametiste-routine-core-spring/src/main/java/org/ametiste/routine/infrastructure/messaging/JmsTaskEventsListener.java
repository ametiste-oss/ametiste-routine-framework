package org.ametiste.routine.infrastructure.messaging;

import org.ametiste.routine.application.service.execution.OrderExecutionGateway;
import org.ametiste.routine.application.service.execution.TaskExecutionService;
import org.ametiste.routine.domain.task.ExecutionOrder;
import org.ametiste.routine.domain.task.OperationTerminatedEvent;
import org.ametiste.routine.domain.task.TaskTerminatedEvent;
import org.ametiste.routine.infrastructure.execution.BoundedExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;

import java.util.UUID;
import java.util.concurrent.Executors;

/**
 *
 * @since
 */
public class JmsTaskEventsListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final TaskExecutionService taskExecutionService;

    private final OrderExecutionGateway orderExecutionGateway;

    /**
     * <p>
     *     Shared tasks executor, note this executor is shared to provide shared limit
     *     for pending and executing tasks.
     * </p>
     */
    private final BoundedExecutor boundedExecutor;

    public JmsTaskEventsListener(
            TaskExecutionService taskExecutionService,
            OrderExecutionGateway orderExecutionGateway,
            int concurrencyLevel) {
        this.taskExecutionService = taskExecutionService;
        this.orderExecutionGateway = orderExecutionGateway;
        this.boundedExecutor = new BoundedExecutor(
                Executors.newFixedThreadPool(concurrencyLevel), concurrencyLevel);
    }

    @JmsListener(destination = "task-issued")
    public void onTaskIssued(UUID taskId) throws Exception {
        boundedExecutor.submitTask(() -> {
            try {
                taskExecutionService.pendTaskForExecution(taskId);
            } catch (Exception e) {
                logger.error("Error during task execution.", e);
                // TODO : add stacktrace to log somehow, or create report, or both
            }
        });
    }

    @JmsListener(destination = "task-terminated")
    public void onTaskTerminated(TaskTerminatedEvent event) throws Exception {
        orderExecutionGateway.terminateOrderExecution(event.taskId());
    }

//    @JmsListener(destination = "task-op-terminated")
//    public void onTaskOpTerminated(OperationTerminatedEvent event) throws Exception {
//        orderExecutionGateway.terminateOrderExecution(event.taskId());
//    }

    @JmsListener(destination = "task-pended")
    public void onTaskPended(ExecutionOrder executionOrder) throws Exception {
        orderExecutionGateway.executeOrder(executionOrder);
    }

}
