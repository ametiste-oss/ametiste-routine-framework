package org.ametiste.routine.infrastructure.persistency.jpa;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.infrastructure.persistency.jpa.data.TaskData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
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
    public Task findTask(UUID taskId) {
        return reflectDataAsTask(jpaTaskDataRepository.findOne(taskId));
    }

    @Override
    @Transactional
    public List<Task> findTasksByState(Task.State state, int limit) {
        return jpaTaskDataRepository.findByState(state).stream().map(
                this::reflectDataAsTask
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveTask(Task task) {
        final JPATaskReflection jpaTaskReflection = new JPATaskReflection();
        task.reflectAs(jpaTaskReflection);
        jpaTaskDataRepository.save(jpaTaskReflection.reflectedTaskData());
    }

    @Override
    @Transactional
    public Task findTaskByOperationId(UUID operationId) {
        return reflectDataAsTask(jpaTaskDataRepository.findByOperationDataId(operationId));
    }

    private Task reflectDataAsTask(TaskData taskData) {
        return new Task(new JPATaskReflection(taskData));
    }

}
