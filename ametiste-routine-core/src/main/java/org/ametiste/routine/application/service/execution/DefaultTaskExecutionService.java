package org.ametiste.routine.application.service.execution;

import org.ametiste.domain.AggregateInstant;
import org.ametiste.routine.application.service.TaskAppEvenets;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 *
 * @since
 */
public class DefaultTaskExecutionService implements TaskExecutionService, ExecutionFeedback {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private TaskRepository taskRepository;

    private final TaskAppEvenets taskAppEvenets;
    private OperationExecutionGateway operationExecutionGateway;

    public DefaultTaskExecutionService(TaskRepository taskRepository,
                                       TaskAppEvenets taskAppEvenets,
                                       OperationExecutionGateway operationExecutionGateway) {
        this.taskRepository = taskRepository;
        this.taskAppEvenets = taskAppEvenets;
        this.operationExecutionGateway = operationExecutionGateway;
    }

    @Override
    public void executeTask(UUID taskId) {
        AggregateInstant.create(taskId, taskRepository::findTask, taskRepository::saveTask)
                .action(t -> { logger.debug("Executing task : {} ", t.entityId()); })
                .action(Task::prepareExecution)
                .consume(taskAppEvenets::taskPended)
                .consume(o -> logger.debug("Pass execution order to operations service: {}", o.executionLines()))
                .done();
    }

    @Override
    public void completeTask(UUID taskId, String withMessage) {
         // TODO: implement, Task need to support complete operation too, this operation
         // required for task timeouts and termination implementation
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
                .action(Task::noticeOperation, operationId, noticeMessage);
    }

    @Override
    public void operationFailed(UUID operationId, String withMessage) {
        taskInstantForOperation(operationId)
                .action(Task::noticeOperation, operationId, withMessage)
                .action(Task::terminateOperation, operationId)
                .done();
    }

    private AggregateInstant<UUID, Task> taskInstantForOperation(UUID operationId) {
        return AggregateInstant.create(operationId,
                taskRepository::findTaskByOperationId, taskRepository::saveTask
        );
    }

}
