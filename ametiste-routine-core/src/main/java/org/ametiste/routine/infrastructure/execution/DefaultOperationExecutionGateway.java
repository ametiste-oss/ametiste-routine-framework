package org.ametiste.routine.infrastructure.execution;

import org.ametiste.metrics.annotations.Timeable;
import org.ametiste.routine.application.service.execution.ExecutionFeedback;
import org.ametiste.routine.application.service.execution.OperationExecutionGateway;
import org.ametiste.routine.infrastructure.protocol.ProtocolGatewayservice;
import org.ametiste.routine.sdk.mod.protocol.ProtocolGateway;
import org.ametiste.routine.sdk.operation.OperationExecutorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

/**
 *
 * <p>
 *     Implementation of {@link OperationExecutionGateway} that executes operation local,
 *     withing the space of current {@code Routine} process.
 * </p>
 *
 * @since 0.1.0
 *
 */
// TODO: rename to LocalOperationExecutionGateway
public class DefaultOperationExecutionGateway implements OperationExecutionGateway {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<String, OperationExecutorFactory> operationExecutors;
    private final ProtocolGatewayservice protocolGatewayservice;

    public DefaultOperationExecutionGateway(
            Map<String, OperationExecutorFactory> operationExecutors,
            ProtocolGatewayservice protocolGatewayservice) {
        this.operationExecutors = operationExecutors;
        this.protocolGatewayservice = protocolGatewayservice;
    }

    @Override
    @Timeable(name = ExecutionMetrics.TASK_EXECUTION_TIMING)
    public void executeOperation(UUID operationId,
                                 String operationExecLine,
                                 Map<String, String> properties,
                                 ExecutionFeedback feedback) {

        if (!operationExecutors.containsKey(operationExecLine)) {
            throw new IllegalStateException("Can't find operation executor for: " + operationExecLine);
        }

        final DefaultOperationFeedbackController feedbackController =
                new DefaultOperationFeedbackController(feedback, operationId);

        final ProtocolGateway protocolGateway =
                protocolGatewayservice.createGateway(operationExecLine);

        feedback.operationStarted(operationId);

        try {
            operationExecutors.get(operationExecLine)
                    .createExecutor()
                    .execOperation(operationId, properties, feedbackController, protocolGateway);
        } catch (Exception e) {
            logger.error("Error during task operation execution.", e);
            feedback.operationFailed(operationId, "Operation failed on execution.");
            return;
        }

        feedback.operationDone(operationId);

    }

}
