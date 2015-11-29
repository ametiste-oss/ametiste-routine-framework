package org.ametiste.routine.infrastructure.persistency.sdata;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.infrastructure.persistency.ClosedTaskReflection;

import java.util.List;
import java.util.UUID;

/**
 *
 * @since
 */
public class SpringDataTaskRepository implements TaskRepository {

    private final JPATaskDataRepository jpaTaskDataRepository;

    public SpringDataTaskRepository(JPATaskDataRepository jpaTaskDataRepository) {
        this.jpaTaskDataRepository = jpaTaskDataRepository;
    }

    @Override
    public Task findTask(UUID taskId) {
        final TaskData taskData = jpaTaskDataRepository.findOne(taskId);
        return new Task(new JPATaskReflection(taskData));
    }

    @Override
    public List<Task> findTasksByState(Task.State state, int limit) {
        return jpaTaskDataRepository.findByState(state);
    }

    @Override
    public void saveTask(Task task) {
        final JPATaskReflection jpaTaskReflection = new JPATaskReflection();
        task.reflectAs(jpaTaskReflection);
        jpaTaskDataRepository.save(jpaTaskReflection.reflectedTaskData());
    }

    @Override
    public Task findTaskByOperationId(UUID operationId) {
        return jpaTaskDataRepository.findByOperationDataId(operationId);
    }
}
