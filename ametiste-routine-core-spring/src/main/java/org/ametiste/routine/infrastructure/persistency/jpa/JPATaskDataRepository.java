package org.ametiste.routine.infrastructure.persistency.jpa;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.infrastructure.persistency.jpa.data.TaskData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 * @since 0.1.0
 */
public interface JPATaskDataRepository extends CrudRepository<TaskData, UUID>,
        JpaSpecificationExecutor<TaskData> {

    TaskData findByOperationDataId(UUID operationId);

    List<TaskData> findByState(Task.State state);

    Page<TaskData> findByStateIn(List<String> state, Pageable page);

    long countByState(String state);

    @Modifying
    @Query("DELETE FROM TaskData t WHERE t.state IN (?1) AND t.completionTime < ?2")
    void deleteByStateAndCompletionDate(List<String> states, Date afterCompletion);

}
