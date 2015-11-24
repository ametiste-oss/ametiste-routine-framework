package org.ametiste.routine.infrastructure.messaging;

import org.ametiste.routine.application.service.execution.TaskExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;

import java.util.UUID;

/**
 *
 * @since
 */
public class JmsIssuedTaskEventListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private TaskExecutionService taskExecutionService;

    public JmsIssuedTaskEventListener(TaskExecutionService taskExecutionService) {
        this.taskExecutionService = taskExecutionService;
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

}
