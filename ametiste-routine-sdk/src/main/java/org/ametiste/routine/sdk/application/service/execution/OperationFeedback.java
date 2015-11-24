package org.ametiste.routine.sdk.application.service.execution;

/**
 *
 * @since
 */
public interface OperationFeedback {

    void operationStarted(String withMessage);

    void operationDone(String withMessage);

    void operationNotice(String noticeMessage);

    void operationFailed(String withMessage);

    default void operationSucceed() {
        operationDone("SUCCEED");
    };

}
