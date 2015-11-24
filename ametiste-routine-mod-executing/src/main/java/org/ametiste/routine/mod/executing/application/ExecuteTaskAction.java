package org.ametiste.routine.mod.executing.application;

import org.ametiste.routine.application.service.execution.TaskExecutionService;
import org.ametiste.routine.domain.log.TaskLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

/**
 *
 * @since
 */
public class ExecuteTaskAction {

    public static final int MAX_APPENDING_COUNT = 300;

    public static final String BLANK_RUN_EVENT = "executeTask.blankRun";

    public static final String EFFICIENT_RUN_EVENT = "executeTask.efficientRun";

    private final TaskExecutionService taskExecutionService;

    private final TaskLogRepository taskLogRepository;

    // private final ActionActuatorDelegate actionActuatorDelegate;

    // TODO : need interface to change this parameter at runtime
    private int appendingCount;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ExecuteTaskAction(TaskExecutionService taskExecutionService,
                             TaskLogRepository taskLogRepository,
                             // ActionActuatorDelegate actionActuatorDelegate,
                             int appendingCount) {

        validateAppendingCount(appendingCount);

        this.taskExecutionService = taskExecutionService;
        this.taskLogRepository = taskLogRepository;
        // this.actionActuatorDelegate = actionActuatorDelegate;
        this.appendingCount = appendingCount;
    }

    public void executeAction() {

        final long executingTasksCount = taskLogRepository.countActiveTasks();
        final long appendCount;

        if ( executingTasksCount < appendingCount) {
            appendCount = appendingCount - executingTasksCount;
        } else {
            // TODO: need to report if executingTasksCount is too long, I guess I need global warning system.
            return;
        }

        final List<UUID> newTasks = taskLogRepository.findNewTasks(appendCount);

        // calculateBlankRuns(newTasks);

        if (logger.isDebugEnabled()) {
            logger.debug("Trying to execute tasks.");
            logger.debug("Executing tasks count : " + executingTasksCount);
            logger.debug("Appending tasks count : " + appendCount);
            logger.debug("New tasks count : " + newTasks.size());
        }

        for (UUID newTask : newTasks) {
            try {
                taskExecutionService.executeTask(newTask);
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Error during recovery task execution.", e);
                }

                // TODO : add stacktrace to log somehow, or create report, or both

            }
        }

    }

    /*
    private void calculateBlankRuns(List<UUID> newTasks) {
        if (newTasks.size() == 0) {
            actionActuatorDelegate.produceEvent(BLANK_RUN_EVENT);
        } else {
            actionActuatorDelegate.produceEvent(EFFICIENT_RUN_EVENT);
        }
    }*/

    public static void validateAppendingCount(int appendingCount) {
        if (appendingCount > MAX_APPENDING_COUNT || appendingCount < 0) {
            throw new IllegalArgumentException("appendingCount must be " +
                    "positive, max appendingCount values is " + MAX_APPENDING_COUNT);
        }
    }

}
