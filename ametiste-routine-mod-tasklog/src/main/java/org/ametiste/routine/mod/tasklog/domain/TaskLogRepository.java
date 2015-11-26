package org.ametiste.routine.mod.tasklog.domain;

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

    int countEntriesByStatus(String byStatus);

}
