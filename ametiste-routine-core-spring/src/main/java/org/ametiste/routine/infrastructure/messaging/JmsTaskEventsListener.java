package org.ametiste.routine.infrastructure.messaging;

import org.ametiste.routine.application.service.execution.TaskExecutionService;
import org.ametiste.routine.domain.task.ExecutionLine;
import org.ametiste.routine.domain.task.ExecutionOrder;
import org.ametiste.routine.application.service.execution.ExecutionFeedback;
import org.ametiste.routine.application.service.execution.OperationExecutionGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;

import java.util.UUID;

/**
 *
 * @since
 */
public class JmsTaskEventsListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final TaskExecutionService taskExecutionService;
    private final ExecutionFeedback executionFeedback;
    private final OperationExecutionGateway executionManager;

    public JmsTaskEventsListener(
            TaskExecutionService taskExecutionService,
            ExecutionFeedback executionFeedback,
            OperationExecutionGateway executionManager) {
        this.taskExecutionService = taskExecutionService;
        this.executionFeedback = executionFeedback;
        this.executionManager = executionManager;
    }

    @JmsListener(destination = "task-issued", containerFactory = "issuedTasksListenerContainerFactory")
    public void onTaskIssued(UUID taskId) {
        try {
            taskExecutionService.executeTask(taskId);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Error during task execution.", e);
            }
            // TODO : add stacktrace to log somehow, or create report, or both
        }
    }

    @JmsListener(destination = "task-pended", containerFactory = "issuedTasksListenerContainerFactory")
    public void onTaskPended(ExecutionOrder executionOrder) {
        for (ExecutionLine line : executionOrder.executionLines()) {

            if (logger.isDebugEnabled()) {
                logger.debug("Pass execution line to operations service: {}", line.line());
            }

            // TODO: timeouts?
            executionManager.executeOperation(
                    line.operationId(), line.line(), line.properties(), executionFeedback
            );
        }
    }

}
