package org.ametiste.routine.application.service.execution;

import java.util.UUID;

/**
 *
 * @since
 */
public interface ExecutionFeedback {

    void operationStarted(UUID operationId);

    void operationDone(UUID operationId);

    void operationStarted(UUID operationId, String withMessage);

    void operationDone(UUID operationId, String withMessage);

    void operationNotice(UUID operationId, String noticeMessage);

    void operationFailed(UUID operationId, String withMessage);

}
