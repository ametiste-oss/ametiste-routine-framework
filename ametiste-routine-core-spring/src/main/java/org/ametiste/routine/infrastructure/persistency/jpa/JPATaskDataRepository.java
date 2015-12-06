package org.ametiste.routine.infrastructure.persistency.jpa;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.infrastructure.persistency.jpa.data.TaskData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

/**
 *
 * @since 0.1.0
 */
public interface JPATaskDataRepository extends CrudRepository<TaskData, UUID> {

    TaskData findByOperationDataId(UUID operationId);

    List<TaskData> findByState(Task.State state);

    Page<TaskData> findByStateIn(List<String> state, Pageable page);

    long countByState(String state);
}
