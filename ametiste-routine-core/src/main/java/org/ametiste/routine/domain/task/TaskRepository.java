package org.ametiste.routine.domain.task;

import org.ametiste.routine.sdk.domain.TaskFilter;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 *
 * @since
 */
public interface TaskRepository {

    /**
     *
     * <p>
     * Note, implentations should provide transactional behaviour for this method,
     * it means if there is aggregate that was found by this method, their state
     * may be changed only by {@link #saveTask(Task)} method invoked within this transaction.
     * </p>
     *
     * <p>
     * For example, for MySQL implementation it means, that the 'SELECT FOR UPDATE' must be used
     * to retrieve aggregate data.
     * </p>
     *
     * @param taskId task identifier for lookup
     * @return task aggregate
     */
    // TODO : need TaskNotFoundException here
    Task findTask(UUID taskId);

    List<Task> findTasksByState(Task.State state, int limit);

    List<Task> findTasksByState(List<Task.State> state, int limit);

    void saveTask(Task task);

    Task findTaskByOperationId(UUID operationId);

    List<Task> findTasks(Consumer<TaskFilter> filterBuilder);

    long countTasks(Consumer<TaskFilter> filterBuilder);

    void deleteTasks(List<Task.State> states, Instant after);
}
