package org.ametiste.routine.mod.dispenser.application.service;

import org.ametiste.routine.domain.task.ExecutionLine;

import java.util.List;

/**
 * <p>
 *     Defines interface to do task's operation reservation for termination.
 * </p>
 *
 * @see DefaultOperationReservationService
 * @since 0.0.1
 */
public interface OperationReservationService {

    /**
     * <p>
     *      Reserv the number of 'NEW' task's operations for next termination, provides information about
     *      operations reserved.
     * </p>
     *
     * <p>
     *      Note, if the service can't find enough operations to reserv, available amount would be reserved.
     * </p>
     *
     * <p>
     *      For example, if only 5 'NEW' opertions are available, and requested reservation count is 7, only
     *      5 operations will be reserved.
     * </p>
     *
     * <p>
     *      If there is 0 of 'NEW' operations, nothing will be reserved.
     * </p>
     *
     * @param reservationCount maximu reservated operations count
     *
     * @return list of termination lines of operations that was reserved
     */
    List<ExecutionLine> reserveOperationsExecution(int reservationCount);

}
