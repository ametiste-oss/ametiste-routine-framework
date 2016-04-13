package org.ametiste.routine.infrastructure.execution.local;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.laplatform.protocol.gateway.ProtocolGatewayService;
import org.ametiste.laplatform.sdk.protocol.ProtocolFactory;
import org.ametiste.metrics.annotations.Timeable;
import org.ametiste.routine.domain.scheme.OperationScheme;
import org.ametiste.routine.domain.scheme.SchemeRepository;
import org.ametiste.routine.domain.task.ExecutionLine;
import org.ametiste.routine.infrastructure.execution.LineExecutionGateway;
import org.ametiste.routine.infrastructure.execution.OperationRuntime;
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
    private final List<OperationRuntime<ProtocolFactory<?>>> runtimeProtocols;
    private final TaskExecutionController feedback;

    public LocalLineExecutionGateway(
            SchemeRepository schemeRepository,
            TaskExecutionController taskExecutionController,
            ProtocolGatewayService protocolGatewayservice,
            List<OperationRuntime<ProtocolFactory<?>>> runtimeProtocols) {
        this.schemeRepository = schemeRepository;
        this.feedback = taskExecutionController;
        this.runtimeProtocols = runtimeProtocols;
        this.protocolGatewayservice = protocolGatewayservice;
    }

    @Override
    @Timeable(name = ExecutionMetrics.GATEWAY_EXECUTION_TIMING)
    public void executeOperation(UUID taskId, ExecutionLine executionLine) {

        final OperationScheme operationScheme = schemeRepository
                .findOperationScheme(executionLine.operationName());

        final LocalOperationFeedbackController feedbackController =
                new LocalOperationFeedbackController(feedback, executionLine.operationId());

        // NOTE: creating protocols that bound to concrete operation runtime
        final List<ProtocolFactory<?>> runtimeProtocolFactories = runtimeProtocols.stream().map(
                f -> f.createRuntimeBoundObject(taskId, executionLine, operationScheme, feedbackController)
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
                // NOTE: this error is not thrown, so the client will continue to execute task operations,
                // so the next operations will fail cos the task is already done
                logger.error("Error during task operation execution : " + executionLine.operationName(), e);
            }
            feedback.operationFailed(executionLine.operationId(), "Operation failed on execution.");
        } finally {
            protocolGateway.release();
        }

    }

}
