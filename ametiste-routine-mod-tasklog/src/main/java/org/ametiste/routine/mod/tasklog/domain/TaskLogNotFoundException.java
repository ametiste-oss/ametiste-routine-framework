package org.ametiste.routine.mod.tasklog.domain;

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
