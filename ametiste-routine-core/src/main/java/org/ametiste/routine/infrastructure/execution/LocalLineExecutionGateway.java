package org.ametiste.routine.infrastructure.execution;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.laplatform.protocol.gateway.ProtocolGatewayService;
import org.ametiste.metrics.annotations.Timeable;
import org.ametiste.routine.application.service.execution.ExecutionFeedback;
import org.ametiste.routine.application.service.execution.LineExecutionGateway;
import org.ametiste.routine.domain.task.ExecutionLine;
import org.ametiste.routine.sdk.operation.OperationExecutorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 *
 * <p>
 *     Implementation of {@link LineExecutionGateway} that executes operation local,
 *     withing the space of current {@code Routine} process.
 * </p>
 *
 * @since 0.1.0
 *
 */
public class LocalLineExecutionGateway implements LineExecutionGateway {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<String, OperationExecutorFactory> operationExecutors;

    private final ExecutionFeedback feedback;

    private final ProtocolGatewayService protocolGatewayservice;

    public LocalLineExecutionGateway(
            Map<String, OperationExecutorFactory> operationExecutors,
            ProtocolGatewayService protocolGatewayservice,
            ExecutionFeedback executionFeedback) {
        this.operationExecutors = operationExecutors;
        this.protocolGatewayservice = protocolGatewayservice;
        this.feedback = executionFeedback;
    }

    @Override
    @Timeable(name = ExecutionMetrics.GATEWAY_EXECUTION_TIMING)
    public void executeOperation(ExecutionLine executionLine) {

        if (!operationExecutors.containsKey(executionLine.operationName())) {
            throw new IllegalStateException("Can't find operation executor for: " +
                    executionLine.operationName());
        }

        final LocalOperationFeedbackController feedbackController =
                new LocalOperationFeedbackController(feedback, executionLine.operationId());

        final ProtocolGateway protocolGateway =
                protocolGatewayservice.createGateway(executionLine.operationName());

        feedback.operationStarted(executionLine.operationId());

        try {
            operationExecutors.get(executionLine.operationName())
                    .createExecutor()
                    .execOperation(
                        executionLine.operationId(),
                        executionLine.properties(),
                        feedbackController,
                        protocolGateway
                    );
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.error("Error during task operation execution.", e);
            }
            feedback.operationFailed(executionLine.operationId(), "Operation failed on execution.");
            return;
        }

        feedback.operationDone(executionLine.operationId());

    }

}
