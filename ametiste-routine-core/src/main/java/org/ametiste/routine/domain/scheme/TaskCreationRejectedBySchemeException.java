package org.ametiste.routine.domain.scheme;

/**
 *
 * @since
 */
public class TaskCreationRejectedBySchemeException extends Exception {

    public TaskCreationRejectedBySchemeException(String message) {
        super(message);
    }

    public TaskCreationRejectedBySchemeException(String message, Throwable cause) {
        super(message, cause);
    }

}
