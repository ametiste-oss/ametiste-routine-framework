package org.ametiste.routine.sdk.operation;

/**
 *
 * @since
 */
public interface OperationFeedback {

    void operationNotice(String noticeMessage);

    void operationFailed(String withMessage);

}
