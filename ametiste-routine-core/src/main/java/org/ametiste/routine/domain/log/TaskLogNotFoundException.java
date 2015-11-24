package org.ametiste.routine.domain.log;

/**
 *
 * @since
 */
public class TaskLogNotFoundException extends RuntimeException {

    public TaskLogNotFoundException(String message) {
        super(message);
    }

    public TaskLogNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
