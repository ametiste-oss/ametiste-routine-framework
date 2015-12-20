package org.ametiste.routine.infrastructure.execution;

import org.ametiste.routine.domain.task.ExecutionOrder;

import java.util.UUID;

/**
 * <p>
 *     Interface that defines operations to execute {@link ExecutionOrder}.
 * </p>
 *
 * <p>
 *     Note, order execiton gateway implementation is fully responsible to
 *     organize order termination process.
 * </p>
 *
 * <p>
 *     Gateway may be implemented as local object or as proxy connector
 *     for remote service.
 * </p>
 *
 * @since 0.1.0
 */
public interface TaskExecutionGateway {

    /**
     *  <p>
     *      Starts termination of the task.
     *  </p>
     */
    void startTaskExecution(UUID taskId);

    /**
     *  <p>
     *      Terminates termination of the order bound to the given task.
     *  </p>
     */
    void terminateTaskExecution(UUID taskId);

}
