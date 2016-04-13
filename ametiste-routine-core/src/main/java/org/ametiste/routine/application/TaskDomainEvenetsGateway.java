package org.ametiste.routine.application;

import org.ametiste.routine.domain.task.*;

import java.util.UUID;

/**
 * <p>
 *     General protocol to send various app events through the system.
 * </p>
 *
 * @since 0.1.0
 */
public interface TaskDomainEvenetsGateway {

    void taskIssued(UUID taskId);

    void operationTerminated(OperationTerminatedEvent event);

    void taskTerminated(TaskTerminatedEvent taskTerminatedEvent);

    void taskDone(TaskDoneEvent taskEvent);

}
