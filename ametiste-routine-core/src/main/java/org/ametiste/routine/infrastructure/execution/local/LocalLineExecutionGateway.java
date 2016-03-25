package org.ametiste.routine.infrastructure.execution.local;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.laplatform.protocol.gateway.ProtocolGatewayService;
import org.ametiste.metrics.annotations.Timeable;
import org.ametiste.routine.domain.scheme.OperationScheme;
import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.domain.scheme.TaskSchemeRepository;
import org.ametiste.routine.domain.task.ExecutionLine;
import org.ametiste.routine.infrastructure.execution.LineExecutionGateway;
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

    private final TaskSchemeRepository taskSchemeRepository;

    private final TaskExecutionController feedback;

    private final ProtocolGatewayService protocolGatewayservice;

    public LocalLineExecutionGateway(
            TaskSchemeRepository taskSchemeRepository,
            ProtocolGatewayService protocolGatewayservice,
            TaskExecutionController taskExecutionController) {
        this.taskSchemeRepository = taskSchemeRepository;
        this.protocolGatewayservice = protocolGatewayservice;
        this.feedback = taskExecutionController;
    }

    @Override
    @Timeable(name = ExecutionMetrics.GATEWAY_EXECUTION_TIMING)
    public void executeOperation(ExecutionLine executionLine) {

        OperationScheme operationScheme = taskSchemeRepository
                .findOperationScheme(executionLine.operationName());

        // TODO: move it to repo
        if (operationScheme == null) {
            throw new IllegalStateException("Can't find operation executor for: " +
                    executionLine.operationName());
        }

//        if (!operationExecutors.containsKey(executionLine.operationName())) {
//            throw new IllegalStateException("Can't find operation executor for: " +
//                    executionLine.operationName());
//        }

        final LocalOperationFeedbackController feedbackController =
                new LocalOperationFeedbackController(feedback, executionLine.operationId());

        final ProtocolGateway protocolGateway = protocolGatewayservice.createGateway(
                executionLine.operationName(),
                executionLine.properties()
        );

        feedback.operationStarted(executionLine.operationId());

        try {
            operationScheme.operationExecutor()
                    .execOperation(
                        feedbackController,
                        protocolGateway
                    );
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.error("Error during task operation termination.", e);
            }
            feedback.operationFailed(executionLine.operationId(), "Operation failed on termination.");
            return;
        }

        feedback.operationDone(executionLine.operationId());

    }

}
