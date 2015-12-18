package org.ametiste.routine.domain.task;

import java.io.Serializable;
import java.util.UUID;

/**
 * <p>
 *     Idicates terminated operation of the task. Convey infomration about task and
 *     operation that was terminated.
 * </p>
 *
 * @since 0.1.0
 */
public class OperationTerminatedEvent implements Serializable {

    private final UUID taskId;
    private final UUID operationId;

    OperationTerminatedEvent(UUID taskId, UUID operationId) {
        this.taskId = taskId;
        this.operationId = operationId;
    }

    public UUID taskId() {
        return taskId;
    }

    public UUID operationId() {
        return operationId;
    }

}
