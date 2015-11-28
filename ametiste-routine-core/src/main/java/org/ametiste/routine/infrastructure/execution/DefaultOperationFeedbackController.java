package org.ametiste.routine.infrastructure.execution;

import org.ametiste.routine.application.service.execution.ExecutionFeedback;
import org.ametiste.routine.sdk.operation.OperationFeedback;

import java.util.UUID;

/**
 *
 * @since
 */
class DefaultOperationFeedbackController implements OperationFeedback {

    private final UUID operationId;

    private final ExecutionFeedback feedback;

    DefaultOperationFeedbackController(ExecutionFeedback feedback, UUID operationId) {
        this.feedback = feedback;
        this.operationId = operationId;
    }

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
}
