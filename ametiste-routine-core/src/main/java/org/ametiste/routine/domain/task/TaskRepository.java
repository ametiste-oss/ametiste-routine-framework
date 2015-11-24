package org.ametiste.routine.domain.task;

import java.util.List;
import java.util.UUID;

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

    void saveTask(Task task);

    Task findTaskByOperationId(UUID operationId);

}
