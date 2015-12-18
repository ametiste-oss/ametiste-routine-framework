package org.ametiste.routine.application.service.execution;

import org.ametiste.domain.AggregateInstant;
import org.ametiste.routine.application.service.TaskDomainEvenets;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 *
 * @since
 */
// NOTE: this class methods may throw IllegalStateException,
// since tasks state are eventualy consisten, that is asssumed
// as okay for this version to control tasks state through
// exceptions.


// TODO: Extract ExecutionFeedback implementation as separate class
public class DefaultTaskExecutionService implements TaskExecutionService, ExecutionFeedback {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private TaskRepository taskRepository;

    private final TaskDomainEvenets taskDomainEvenets;

    public DefaultTaskExecutionService(TaskRepository taskRepository,
                                       TaskDomainEvenets taskDomainEvenets) {
        this.taskRepository = taskRepository;
        this.taskDomainEvenets = taskDomainEvenets;
    }

    @Override
    public void pendTaskForExecution(UUID taskId) {
        AggregateInstant.create(taskId, taskRepository::findTask, taskRepository::saveTask)
                .action(t -> { logger.debug("Executing task : {} ", t.entityId()); })
                .action(Task::prepareExecution)
                .consume(taskDomainEvenets::taskPended)
                .consume(o -> logger.debug("Pass execution order to operations service: {}", o.executionLines()))
                .done();
    }

    @Override
    public void terminateTask(UUID taskId, String withMessage) {
        AggregateInstant.create(taskId, taskRepository::findTask, taskRepository::saveTask)
                .action(t -> { return t.terminate(withMessage); })
                .consume(taskDomainEvenets::taskTerminated)
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
                .consume(taskDomainEvenets::operationTerminated)
                .done();
    }

    private AggregateInstant<UUID, Task> taskInstantForOperation(UUID operationId) {
        return AggregateInstant.create(operationId,
                taskRepository::findTaskByOperationId, taskRepository::saveTask
        );
    }

}
