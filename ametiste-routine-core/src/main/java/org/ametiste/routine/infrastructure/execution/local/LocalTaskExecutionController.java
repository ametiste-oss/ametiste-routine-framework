package org.ametiste.routine.infrastructure.execution.local;

import org.ametiste.domain.AggregateInstant;
import org.ametiste.routine.application.CoreEventsGateway;
import org.ametiste.routine.application.TaskDomainEvenetsGateway;
import org.ametiste.routine.domain.task.ExecutionOrder;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.infrastructure.execution.TaskExecutionGateway;

import java.util.UUID;

/**
 *
 * <p>
 *     Implementation of {@link TaskExecutionController} protocol that designed to be used
 *     with local {@link TaskExecutionGateway} implementation.
 * </p>
 *
 * <p>
 *     This class implements termination control directly on domain objects.
 * </p>
 *
 * @since 0.1.0
 */
// NOTE: this class methods may throw IllegalStateException,
// since tasks state are eventualy consisten, that is asssumed
// as okay for this version to control tasks state through
// exceptions.
// TODO: hmm, should I define app-layer services for this operations and use controller only as a proxy for this?
// TODO: I guess yes, cos there is aggregate roots logic.. but it is infrastrucure
public class LocalTaskExecutionController implements TaskExecutionController {

    private final TaskRepository taskRepository;
    private final TaskDomainEvenetsGateway taskDomainEvenetsGateway;
    private final CoreEventsGateway coreEventsGateway;

    public LocalTaskExecutionController(TaskRepository taskRepository,
                                        TaskDomainEvenetsGateway taskDomainEvenetsGateway,
                                        CoreEventsGateway coreEventsGateway) {
        this.taskRepository = taskRepository;
        this.taskDomainEvenetsGateway = taskDomainEvenetsGateway;
        this.coreEventsGateway = coreEventsGateway;
    }

    @Override
    public ExecutionOrder startTaskExecution(UUID taskId) {
        return AggregateInstant.create(taskId, taskRepository::findTask, taskRepository::saveTask)
                .action(Task::prepareExecution)
                .done();
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
