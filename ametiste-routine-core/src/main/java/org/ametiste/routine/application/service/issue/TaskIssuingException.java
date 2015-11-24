package org.ametiste.routine.application.service.issue;

/**
 *
 * @since
 */
public class TaskIssuingException extends RuntimeException {

    public TaskIssuingException(String message) {
        super(message);
    }

    public TaskIssuingException(String message, Throwable cause) {
        super(message, cause);
    }

}
