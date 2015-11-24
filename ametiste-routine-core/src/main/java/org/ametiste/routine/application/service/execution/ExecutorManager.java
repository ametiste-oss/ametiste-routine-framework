package org.ametiste.routine.application.service.execution;

import org.ametiste.routine.sdk.application.service.execution.ExecutionFeedback;
import org.ametiste.routine.sdk.application.service.execution.ExecutionManager;
import org.ametiste.routine.sdk.application.service.execution.OperationExecutorFactory;
import org.ametiste.routine.sdk.application.service.execution.OperationFeedback;

import java.util.Map;
import java.util.UUID;

/**
 *
 * <p><b>WARNING: PoC, PLEASE DONT USE IN PRODUCTION</b></p>
 *
 * @since
 */
public class ExecutorManager implements ExecutionManager {

    private final Map<String, OperationExecutorFactory> operationExecutors;

    public ExecutorManager(Map<String, OperationExecutorFactory> operationExecutors) {
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
