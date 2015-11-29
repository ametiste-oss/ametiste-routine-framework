package org.ametiste.routine.infrastructure.persistency.sdata;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.infrastructure.persistency.ClosedTaskReflection;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

/**
 *
 * @since
 */
public interface JPATaskDataRepository extends CrudRepository<TaskData, UUID> {

    List<Task> findByState(Task.State state);

    Task findByOperationDataId(UUID operationId);

}
