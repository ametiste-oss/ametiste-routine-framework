package org.ametiste.routine.application.events;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @since
 */
public class TaskIssuedEvent implements Serializable {

    private final UUID taskId;
    private final String issuerId;

    public TaskIssuedEvent(UUID taskId, String issuerId) {
        this.taskId = taskId;
        this.issuerId = issuerId;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public String getIssuerId() {
        return issuerId;
    }

}
