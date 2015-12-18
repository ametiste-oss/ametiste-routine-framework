package org.ametiste.routine.infrastructure.execution;

import org.ametiste.routine.application.service.execution.LineExecutionGateway;
import org.ametiste.routine.application.service.execution.OrderExecutionGateway;
import org.ametiste.routine.domain.task.ExecutionLine;
import org.ametiste.routine.domain.task.ExecutionOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * <p>
 *     Local order execution gateway that executes orders in a scope of
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
public class LocalOrderExecutionGateway implements OrderExecutionGateway {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final LineExecutionGateway lineExecutionGateway;
    private final BoundedExecutor boundedExecutor;
    private final ConcurrentHashMap<UUID, Future<?>> executingOrders = new ConcurrentHashMap<>();

    public LocalOrderExecutionGateway(LineExecutionGateway lineExecutionGateway,
                                      BoundedExecutor boundedExecutor) {
        this.lineExecutionGateway = lineExecutionGateway;
        this.boundedExecutor = boundedExecutor;
    }

    @Override
    public void executeOrder(ExecutionOrder executionOrder) {

        try {

            final Future<?> submitedExecution = boundedExecutor.submitTask(() -> {
                for (ExecutionLine line : executionOrder.executionLines()) {

                    if (logger.isDebugEnabled()) {
                        logger.debug("Pass execution operationName to operations service: {}", line.operationName());
                    }

                    // TODO: timeouts?
                    try {
                        lineExecutionGateway.executeOperation(line);
                    } catch (Exception e) {
                        logger.error("Error during execution order.", e);
                        // TODO: custom exception
                        throw new RuntimeException("Can't execute order.", e);
                    } finally {
                        executingOrders.remove(executionOrder.boundTaskId());
                    }
                }
            });

            executingOrders.put(executionOrder.boundTaskId(), submitedExecution);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(executingOrders.size());

    }

    @Override
    public void terminateOrderExecution(UUID boundTaskId) {
        executingOrders.get(boundTaskId).cancel(true);
        executingOrders.remove(boundTaskId);
    }

}
