package org.ametiste.routine.infrastructure.execution;

import org.ametiste.routine.application.service.execution.ExecutionFeedback;
import org.ametiste.routine.sdk.operation.OperationFeedback;

import java.util.UUID;

/**
 *
 * @since
 */
class LocalOperationFeedbackController implements OperationFeedback {

    private final UUID operationId;

    private final ExecutionFeedback feedback;

    LocalOperationFeedbackController(ExecutionFeedback feedback, UUID operationId) {
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
