package org.ametiste.routine.mod.tasklog.domain;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.properties.TaskProperty;

import java.time.Instant;
import java.util.List;
import java.util.Map;
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
    long countActiveTasks();

    @Deprecated
    // TODO: replace by limited version
    List<TaskLogEntry> findEntries();

    TaskLogEntry findTaskLog(UUID taskId);

    List<UUID> findActiveTasksAfterDate(Instant timePoint);

    List<TaskLogEntry> findEntries(List<Task.State> states, int offset, int limit);

    List<TaskLogEntry> findEntries(List<Task.State> states, List<TaskProperty> properties, int offset, int limit);

    /**
     * @deprecated use {@link #countByTaskState(List, List)} instead.
     * @param byStatus
     *
     * @return
     */
    @Deprecated
    int countEntriesByStatus(String byStatus);

    long countByTaskState(List<Task.State> states, List<TaskProperty> properties);

}
