package org.ametiste.routine.application.service.execution;

import org.ametiste.domain.AggregateInstant;
import org.ametiste.routine.application.service.TaskDomainEvenetsGateway;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.TaskRepository;

import java.util.UUID;

/**
 *
 * <p>
 *     Default implementation of {@link ExecutionFeedback} protocol.
 * </p>
 *
 * <p>
 *     Just allows send feedback messages about task execution process,
 *     usually will be used by {@link OrderExecutionGateway} implementations.
 * </p>
 *
 * @since 0.1.0
 */
// NOTE: this class methods may throw IllegalStateException,
// since tasks state are eventualy consisten, that is asssumed
// as okay for this version to control tasks state through
// exceptions.
public class DefaultTaskExecutionFeedbackController implements ExecutionFeedback {

    private final TaskRepository taskRepository;
    private final TaskDomainEvenetsGateway taskDomainEvenetsGateway;

    public DefaultTaskExecutionFeedbackController(TaskRepository taskRepository,
                                                  TaskDomainEvenetsGateway taskDomainEvenetsGateway) {
        this.taskRepository = taskRepository;
        this.taskDomainEvenetsGateway = taskDomainEvenetsGateway;
    }

    @Override
    public void operationStarted(UUID operationId) {
        taskInstantForOperation(operationId)
                .action(Task::executeOperation, operationId)
                .done();
    }

    @Override
    public void operationDone(UUID operationId) {
        taskInstantForOperation(operationId)
                .action(Task::completeOperation, operationId)
                .done();
    }

    @Override
    public void operationStarted(UUID operationId, String withMessage) {
        taskInstantForOperation(operationId)
                .action(Task::noticeOperation, operationId, withMessage)
                .action(Task::executeOperation, operationId)
                .done();
    }

    @Override
    public void operationDone(UUID operationId, String withMessage) {
        taskInstantForOperation(operationId)
                .action(Task::noticeOperation, operationId, withMessage)
                .action(Task::completeOperation, operationId)
                .done();
    }

    @Override
    public void operationNotice(UUID operationId, String noticeMessage) {
        taskInstantForOperation(operationId)
                .action(Task::noticeOperation, operationId, noticeMessage)
                .done();
    }

    @Override
    public void operationFailed(UUID operationId, String withMessage) {
        taskInstantForOperation(operationId)
                .action(Task::noticeOperation, operationId, withMessage)
                .action(t -> { return t.terminateOperation(operationId); })
                .consume(taskDomainEvenetsGateway::operationTerminated)
                .done();
    }

    private AggregateInstant<UUID, Task> taskInstantForOperation(UUID operationId) {
        return AggregateInstant.create(operationId,
                taskRepository::findTaskByOperationId, taskRepository::saveTask
        );
    }

}
