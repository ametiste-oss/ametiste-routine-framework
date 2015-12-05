package org.ametiste.routine.infrastructure.persistency.jpa;

import org.ametiste.metrics.annotations.Timeable;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.infrastructure.persistency.PersistencyMetrics;
import org.ametiste.routine.infrastructure.persistency.jpa.data.TaskData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @since
 */
public class SpringDataTaskRepository implements TaskRepository {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final JPATaskDataRepository jpaTaskDataRepository;

    public SpringDataTaskRepository(JPATaskDataRepository jpaTaskDataRepository) {
        this.jpaTaskDataRepository = jpaTaskDataRepository;
    }

    @Override
    @Transactional
    @Timeable(name = PersistencyMetrics.FIND_TASK_BY_ID_TIMING)
    public Task findTask(UUID taskId) {
        return reflectDataAsTask(jpaTaskDataRepository.findOne(taskId));
    }

    @Override
    @Transactional
    @Timeable(name = PersistencyMetrics.FIND_TASK_BY_STATE)
    public List<Task> findTasksByState(Task.State state, int limit) {
        // TODO: LIMIT parameter is missed!
        return jpaTaskDataRepository.findByState(state).stream().map(
                this::reflectDataAsTask
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Timeable(name = PersistencyMetrics.FIND_TASK_BY_MULTIPLE_STATE)
    public List<Task> findTasksByState(List<Task.State> state, int limit) {
        return jpaTaskDataRepository.findByStateIn(
                state.stream().map(Task.State::name).collect(Collectors.toList()), new PageRequest(0, limit)
        )
            .getContent()
            .stream()
            .map(this::reflectDataAsTask)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Timeable(name = PersistencyMetrics.SAVE_TASK_TIMING)
    public void saveTask(Task task) {
        final JPATaskReflection jpaTaskReflection = new JPATaskReflection();
        task.reflectAs(jpaTaskReflection);
        jpaTaskDataRepository.save(jpaTaskReflection.reflectedTaskData());
    }

    @Override
    @Transactional
    @Timeable(name = PersistencyMetrics.FIND_TASK_BY_OP_ID)
    public Task findTaskByOperationId(UUID operationId) {
        return reflectDataAsTask(jpaTaskDataRepository.findByOperationDataId(operationId));
    }

    private Task reflectDataAsTask(TaskData taskData) {
        return new Task(new JPATaskReflection(taskData));
    }

}
