package org.ametiste.routine.infrastructure.execution.local;

import org.ametiste.routine.sdk.operation.OperationFeedback;

import java.util.UUID;

/**
 *
 * @since
 */
class LocalOperationFeedbackController implements OperationFeedback {

    private final UUID operationId;

    private final TaskExecutionController feedback;

    LocalOperationFeedbackController(TaskExecutionController feedback, UUID operationId) {
        this.feedback = feedback;
        this.operationId = operationId;
    }

    @Override
    public void operationNotice(String noticeMessage) {
        feedback.operationNotice(operationId, noticeMessage);
    }

    @Override
    public void operationFailed(String withMessage) {
        feedback.operationFailed(operationId, withMessage);
    }

}
