package org.ametiste.routine.domain.task;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @since
 */
public class TaskDoneEvent implements Serializable {

    private final UUID taskId;

    TaskDoneEvent(UUID taskId) {
        this.taskId = taskId;
    }

    public UUID taskId() {
        return taskId;
    }

}
