package org.ametiste.routine.mod.tasklog.infrastructure.persistency.jpa;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.infrastructure.persistency.jpa.data.TaskData;
import org.ametiste.routine.infrastructure.persistency.jpa.data.TaskPropertyData;
import org.ametiste.routine.mod.tasklog.domain.TaskLogEntry;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

/**
 *
 * @since
 */
public interface JPATaskLogDataRepository extends CrudRepository<TaskData, UUID>,
        JpaSpecificationExecutor<TaskData> {

    int countTaskByStateIn(List<String> state);

    int countTaskByState(String state);

    int countTaskByStateInAndPropertiesIn(List<String> states, List<TaskPropertyData> properties);

}
