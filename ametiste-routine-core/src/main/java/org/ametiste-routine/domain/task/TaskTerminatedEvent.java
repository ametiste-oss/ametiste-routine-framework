package org.ametiste.routine.domain.task;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 *
 * @since
 */
public class TaskTerminatedEvent implements Serializable {

    private final UUID taskId;
    private final List<UUID> terminatedOperationIds;
    private final String cause;

    TaskTerminatedEvent(UUID taskId, List<UUID> terminatedOperationIds, String cause) {
        this.taskId = taskId;
        this.terminatedOperationIds = terminatedOperationIds;
        this.cause = cause;
    }

    public UUID taskId() {
        return taskId;
    }

    public List<UUID> terminatedOperationIds() {
        return terminatedOperationIds;
    }

    public String cause() {
        return cause;
    }
}
