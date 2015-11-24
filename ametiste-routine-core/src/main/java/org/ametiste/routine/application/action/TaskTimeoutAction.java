package org.ametiste.routine.application.action;

import org.ametiste.routine.application.service.execution.TaskExecutionService;
import org.ametiste.routine.domain.log.TaskLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

/**
 *
 * @since
 */
public class TaskTimeoutAction {

    private TaskExecutionService taskExecutionService;

    private TaskLogRepository taskLogRepository;

    // TODO : need interface to change this parameter at runtime
    private long defaultTimeout;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public TaskTimeoutAction(TaskExecutionService taskExecutionService,
                             TaskLogRepository taskLogRepository,
                             long defaultTimeout) {
        this.taskExecutionService = taskExecutionService;
        this.taskLogRepository = taskLogRepository;
        this.defaultTimeout = defaultTimeout;
    }

    public void executeAction() {

        // TODO: need to add entry to global ApplicationEventsLog that reports about timed out entities

        if (logger.isDebugEnabled()) {
            logger.debug("Trying to execute timeouts.");
        }

        final List<UUID> timedOutTasks = taskLogRepository
                .findActiveTasksAfterDate(Instant.now().minus(defaultTimeout, ChronoUnit.MINUTES));

        timedOutTasks.forEach((taskId) -> {

            if (logger.isDebugEnabled()) {
                logger.debug("Exectuting timeout for : " + taskId);
            }

            try {
                taskExecutionService.completeTask(taskId,
                        String.format("Timedout for %s minutes.", defaultTimeout));
            } catch (Exception e) {
                // NOTE: should try to terminate all timedout tasks.
                if (logger.isDebugEnabled()) {
                    logger.debug("Exception during task timeout execution : " + taskId, e);
                }
            }
        });

    }

}
