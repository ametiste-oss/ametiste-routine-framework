package org.ametiste.routine.domain.scheme;

/**
 * <p>
 *     Base class for all exceptions that may be thrown during TaskScheme execution.
 * </p>
 *
 * @since 0.1.0
 */
public abstract class TaskSchemeException extends Exception {

    public TaskSchemeException(String message) {
        super(message);
    }

    public TaskSchemeException(String message, Throwable cause) {
        super(message, cause);
    }

}
