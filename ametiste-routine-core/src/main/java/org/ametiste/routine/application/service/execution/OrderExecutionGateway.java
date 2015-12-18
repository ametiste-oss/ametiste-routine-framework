package org.ametiste.routine.application.service.execution;

import org.ametiste.routine.domain.task.ExecutionOrder;

import java.util.UUID;

/**
 * <p>
 *     Interface that defines operations to execute {@link ExecutionOrder}.
 * </p>
 *
 * <p>
 *     Note, order execiton gateway implementation is fully responsible to
 *     organize order execution process.
 * </p>
 *
 * <p>
 *     Gateway may be implemented as local object or as proxy connector
 *     for remote service.
 * </p>
 *
 * @since 0.1.0
 */
public interface OrderExecutionGateway {

    /**
     *  <p>
     *      Starts execution of the execution order.
     *  </p>
     */
    void executeOrder(ExecutionOrder executionOrder);

    /**
     *  <p>
     *      Terminates execution of the order bound to the given task.
     *  </p>
     */
    void terminateOrderExecution(UUID boundTaskId);

}
