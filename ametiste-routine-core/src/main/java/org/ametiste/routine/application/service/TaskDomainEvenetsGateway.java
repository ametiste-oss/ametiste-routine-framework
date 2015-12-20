package org.ametiste.routine.application.service;

import org.ametiste.routine.domain.task.ExecutionOrder;
import org.ametiste.routine.domain.task.OperationTerminatedEvent;
import org.ametiste.routine.domain.task.TaskTerminatedEvent;

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

}
