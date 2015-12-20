package org.ametiste.routine.infrastructure.execution.local;

import org.ametiste.routine.domain.task.ExecutionLine;
import org.ametiste.routine.domain.task.ExecutionOrder;
import org.ametiste.routine.infrastructure.execution.LineExecutionGateway;
import org.ametiste.routine.infrastructure.execution.TaskExecutionGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * <p>
 *     Local task execution gateway implementation, designed to execute task orders in a scope of
 *     enclosing {@code Routine} process.
 * </p>
 *
 * <p>
 *     Actual line execution is passing to {@link LineExecutionGateway} implementations.
 * </p>
 *
 * <p>
 *     Note, this object is stateful and maintain internal thread pool to control lines execution.
 * </p>
 *
 * @since 0.1.0
 */
public class LocalTaskExecutionGateway implements TaskExecutionGateway {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final LineExecutionGateway lineExecutionGateway;
    private final BoundedExecutor boundedExecutor;
    private final TaskExecutionController taskExecutionController;
    private final ConcurrentHashMap<UUID, Future<?>> executingOrders = new ConcurrentHashMap<>();

    public LocalTaskExecutionGateway(LineExecutionGateway lineExecutionGateway,
                                     BoundedExecutor boundedExecutor,
                                     TaskExecutionController taskExecutionController) {
        this.lineExecutionGateway = lineExecutionGateway;
        this.boundedExecutor = boundedExecutor;
        this.taskExecutionController = taskExecutionController;
    }

    @Override
    public void startTaskExecution(UUID taskId) {

        try {

            final Future<?> submitedExecution = boundedExecutor.submitTask(() -> {

                final ExecutionOrder order =
                        taskExecutionController.startTaskExecution(taskId);

                for (ExecutionLine line : order.executionLines()) {

                    if (logger.isDebugEnabled()) {
                        logger.debug("Pass termination operationName to operations service: {}", line.operationName());
                    }

                    // TODO: timeouts?
                    try {
                        lineExecutionGateway.executeOperation(line);
                    } catch (Exception e) {
                        logger.error("Error during termination order.", e);
                        // TODO: custom exception
                        throw new RuntimeException("Can't execute order.", e);
                    } finally {
                        executingOrders.remove(order.boundTaskId());
                    }
                }

            });

            // TODO: ffs
            executingOrders.put(taskId, submitedExecution);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void terminateTaskExecution(UUID taskId) {
        executingOrders.get(taskId).cancel(true);
        executingOrders.remove(taskId);
    }

}
