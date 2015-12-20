package org.ametiste.routine.application.action;

import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 *
 * @since
 */
public class RetryFailedAction {

    public final static String ACTION_IDENTIFIER = "core:retry-failed-action";

    private TaskIssueService taskIssueService;

    private final TaskRepository taskRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public RetryFailedAction(TaskIssueService taskIssueService,
                             TaskRepository taskRepository) {
        this.taskIssueService = taskIssueService;
        this.taskRepository = taskRepository;
    }

    public void executeAction() {

        // TODO: at the moment there is problem that the task already scheduled for recovery may be
        // scheduled second time.
        // I guess I just need some kind of constraint at the "routine.core.retryScheme" that does
        // not allow to schedule more than one task for recovery of the given task

        // TODO: add scheme definition and operation executors

        final List<Task> tasksByState =
                taskRepository.findTasksByState(Task.State.TERMINATED, 100);

        for (Task task : tasksByState) {
            try {
                taskIssueService.issueTask(
                        "routine.core.retryScheme",
                        Collections.singletonMap("recovered.task.id", task.entityId().toString()),
                        ACTION_IDENTIFIER
                );
            } catch (Exception e) {
                logger.error("Error during issue task for recovery of {}", task.entityId());
            }
        }

        // TODO: add 2 app events, recovery scheduled event and recovery shedule failed event
        // with lists of task identifiers for each category.

    }

}
