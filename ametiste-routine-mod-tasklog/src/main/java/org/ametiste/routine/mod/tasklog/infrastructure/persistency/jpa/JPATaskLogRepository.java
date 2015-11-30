package org.ametiste.routine.mod.tasklog.infrastructure.persistency.jpa;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.infrastructure.persistency.jpa.data.TaskData;
import org.ametiste.routine.infrastructure.persistency.jpa.data.TaskPropertyData;
import org.ametiste.routine.mod.tasklog.domain.TaskLogEntry;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

/**
 *
 * @since
 */
public interface JPATaskLogRepository extends CrudRepository<TaskData, UUID> {

    int countTaskByStateIn(List<String> state);

    int countTaskByState(String state);

    int countTaskByStateInAndPropertiesIn(List<String> states, List<TaskPropertyData> properties);

    Slice<TaskData> findTaskByStateIn(List<String> states, Pageable pageable);

    Slice<TaskData> findTaskByStateInAndPropertiesNameInAndPropertiesValueIn(List<String> states,
                                                                             List<String> propertiesName,
                                                                             List<String> propertiesValue,
                                                                             Pageable pageable);
}
