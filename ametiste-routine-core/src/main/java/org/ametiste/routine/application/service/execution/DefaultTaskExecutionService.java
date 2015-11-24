package org.ametiste.routine.application.service.execution;

import org.ametiste.routine.domain.task.ExecutionLine;
import org.ametiste.routine.domain.task.ExecutionOrder;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.sdk.application.service.execution.ExecutionFeedback;
import org.ametiste.routine.sdk.application.service.execution.ExecutionManager;
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

    private ExecutionManager executionManager;

    public DefaultTaskExecutionService(TaskRepository taskRepository, ExecutionManager executionManager) {
        this.taskRepository = taskRepository;
        this.executionManager = executionManager;
    }

    @Override
    public void executeTask(UUID taskId) {

        if (logger.isDebugEnabled()) {
            logger.debug("Executing task : " + taskId);
        }

        final ExecutionOrder executionOrder = issueExecutionOrder(taskId);

        if (logger.isDebugEnabled()) {
            logger.debug("Pass execution order to operations service: {}", executionOrder.executionLines());
        }

        for (ExecutionLine line : executionOrder.executionLines()) {

            if (logger.isDebugEnabled()) {
                logger.debug("Pass execution line to operations service: {}", line.line());
            }

            // TODO: timeouts?
            executionManager.executeOperation(
                line.operationId(), line.line(), line.properties(), this
            );
        }
    }

    @Override
    public void completeTask(UUID taskId, String withMessage) {
         // TODO: implement, Task need to support complete operation too, this operation
         // required for task timeouts and termination implementation

    }

    /**
     * <p>
     * Loads task and issue this task execution order.
     * </p>
     *
     * <p>
     * Note, this method is transactional, after method completeon, the state of task
     * will be changed.
     * </p>
     *
     * <p>
     * Transaction would be completed before any messages to external services sent to get consistent
     * state of the domain.
     * </p>
     *
     * @param taskId target task identifier
     * @return task execution order
     */
    // @Transactional
    private ExecutionOrder issueExecutionOrder(UUID taskId) {

        final Task task = taskRepository.findTask(taskId);
        final ExecutionOrder executionOrder = task.prepareExecution();
        taskRepository.saveTask(task);

        return executionOrder;
    }

    @Override
    public void operationStarted(UUID operationId, String withMessage) {
        final Task task = taskRepository.findTaskByOperationId(operationId);
        task.executeOperation(operationId, withMessage);
        taskRepository.saveTask(task);
    }

    @Override
    public void operationDone(UUID operationId, String withMessage) {
        final Task task = taskRepository.findTaskByOperationId(operationId);
        task.completeOperation(operationId, withMessage);
        taskRepository.saveTask(task);
    }

    @Override
    public void operationNotice(UUID operationId, String noticeMessage) {
        throw new RuntimeException("Not implemented yet.");
    }

    @Override
    public void operationFailed(UUID operationId, String withMessage) {
        final Task task = taskRepository.findTaskByOperationId(operationId);
        task.terminateOperation(operationId, withMessage);
        taskRepository.saveTask(task);
    }
}
