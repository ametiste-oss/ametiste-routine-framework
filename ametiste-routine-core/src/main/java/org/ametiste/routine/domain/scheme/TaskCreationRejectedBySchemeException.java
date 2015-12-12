package org.ametiste.routine.domain.scheme;

/**
 * <p>
 *    This exception indicates violation of a constraint during TaskScheme execution.
 * </p>
 *
 * <p>
 *     Usually will be thrown in environmnts where some tasks can't be created by
 *     a scheme for some reasons.
 * </p>
 *
 * <p>
 *     For example, this exception may be thrown if scheme does not allow tasks creation
 *     for some roles.
 * </p>
 *
 * @since 0.1.0
 */
public class TaskCreationRejectedBySchemeException extends TaskSchemeException {

    public TaskCreationRejectedBySchemeException(String message) {
        super(message);
    }

    public TaskCreationRejectedBySchemeException(String message, Throwable cause) {
        super(message, cause);
    }

}
