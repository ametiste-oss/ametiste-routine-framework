package org.ametiste.routine.infrastructure.execution;

import org.ametiste.routine.application.service.execution.ExecutionFeedback;
import org.ametiste.routine.application.service.execution.OperationExecutionGateway;
import org.ametiste.routine.sdk.application.service.execution.OperationExecutorFactory;
import org.ametiste.routine.sdk.application.service.execution.OperationFeedback;

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
public class DefaultOperationExecutionGateway implements OperationExecutionGateway {

    private final Map<String, OperationExecutorFactory> operationExecutors;

    public DefaultOperationExecutionGateway(Map<String, OperationExecutorFactory> operationExecutors) {
        this.operationExecutors = operationExecutors;
    }

    @Override
    public void executeOperation(UUID operationId,
                                 String operationExecLine,
                                 Map<String, String> properties,
                                 ExecutionFeedback feedback) {

        if (!operationExecutors.containsKey(operationExecLine)) {
            throw new IllegalStateException("Can't find operation executor for: " + operationExecLine);
        }

        operationExecutors.get(operationExecLine)
                .createExecutor()
                .execOperation(operationId, properties, new OperationFeedback() {
                    @Override
                    public void operationStarted(String withMessage) {
                        feedback.operationStarted(operationId, withMessage);
                    }
                    @Override
                    public void operationDone(String withMessage) {
                        feedback.operationDone(operationId, withMessage);
                    }
                    @Override
                    public void operationNotice(String noticeMessage) {
                        feedback.operationNotice(operationId, noticeMessage);
                    }
                    @Override
                    public void operationFailed(String withMessage) {
                        feedback.operationFailed(operationId, withMessage);
                    }
                });

    }

}
