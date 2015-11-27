package org.ametiste.routine.mod.tasklog.domain;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.properties.TaskProperty;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 *
 * @since
 */
public interface TaskLogRepository {

    /**
     * This method provides a count of 'PENDING' and 'EXECUTING' tasks.
     *
     * @return
     */
    // TODO: must return int
    long countActiveTasks();

    List<UUID> findNewTasks(long appendCount);

    void saveTaskLog(TaskLogEntry taskLogEntry);

    List<TaskLogEntry> findEntries();

    TaskLogEntry findTaskLog(UUID taskId);

    List<UUID> findActiveTasksAfterDate(Instant timePoint);

    List<TaskLogEntry> findEntries(String byStatus, int offset, int limit);

    /**
     * @deprecated use {@link #countByTaskState(Task.State...)} instead.
     * @param byStatus
     *
     * @return
     */
    @Deprecated
    int countEntriesByStatus(String byStatus);

    int countByTaskState(Task.State[] states, TaskProperty[] properties);

    int countByTaskState(Task.State[] states);
}
