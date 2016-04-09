package org.ametiste.routine.infrastructure.execution.local;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.laplatform.protocol.gateway.ProtocolGatewayService;
import org.ametiste.laplatform.sdk.protocol.ProtocolFactory;
import org.ametiste.metrics.annotations.Timeable;
import org.ametiste.routine.domain.scheme.OperationScheme;
import org.ametiste.routine.domain.scheme.SchemeRepository;
import org.ametiste.routine.domain.task.ExecutionLine;
import org.ametiste.routine.infrastructure.execution.LineExecutionGateway;
import org.ametiste.routine.infrastructure.execution.OperationRuntimeProtocolFactory;
import org.ametiste.routine.sdk.protocol.operation.OperationMetaProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    private final SchemeRepository schemeRepository;
    private final ProtocolGatewayService protocolGatewayservice;
    private final List<OperationRuntimeProtocolFactory<?>> runtimeProtocols;
    private final TaskExecutionController feedback;

    public LocalLineExecutionGateway(
            SchemeRepository schemeRepository,
            TaskExecutionController taskExecutionController,
            ProtocolGatewayService protocolGatewayservice,
            List<OperationRuntimeProtocolFactory<?>> runtimeProtocols) {
        this.schemeRepository = schemeRepository;
        this.feedback = taskExecutionController;
        this.runtimeProtocols = runtimeProtocols;
        this.protocolGatewayservice = protocolGatewayservice;
    }

    @Override
    @Timeable(name = ExecutionMetrics.GATEWAY_EXECUTION_TIMING)
    public void executeOperation(ExecutionLine executionLine) {

        final OperationScheme operationScheme = schemeRepository
                .findOperationScheme(executionLine.operationName());

        final LocalOperationFeedbackController feedbackController =
                new LocalOperationFeedbackController(feedback, executionLine.operationId());

        // NOTE: creating protocols that depends on concrete operation runtime
        final List<ProtocolFactory<?>> runtimeProtocolFactories = runtimeProtocols.stream().map(
                f -> f.runtimeProtocolFactory(executionLine, operationScheme, feedbackController)
        ).collect(Collectors.toList());

        final ProtocolGateway protocolGateway = protocolGatewayservice.createGateway(
                executionLine.operationName(),
                executionLine.properties(),
                runtimeProtocolFactories
        );

        try {
            feedback.operationStarted(executionLine.operationId());
            operationScheme.operationExecutor().execOperation(
                        feedbackController,
                        protocolGateway
                    );
            feedback.operationDone(executionLine.operationId());
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.error("Error during task operation termination.", e);
            }
            feedback.operationFailed(executionLine.operationId(), "Operation failed on termination.");
        } finally {
            protocolGateway.release();
        }

    }

}
