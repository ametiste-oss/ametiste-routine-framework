package org.ametiste.routine.mod.backlog.application.service;

import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.mod.backlog.application.scheme.BacklogRenewTaskScheme;
import org.ametiste.routine.mod.backlog.domain.Backlog;
import org.ametiste.routine.mod.tasklog.domain.TaskLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @since 0.1.0
 */
public class BacklogRenewService {

    private final TaskLogRepository taskLogRepository;

    private final TaskIssueService taskIssueService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public BacklogRenewService(TaskLogRepository taskLogRepository, TaskIssueService taskIssueService) {
        this.taskLogRepository = taskLogRepository;
        this.taskIssueService = taskIssueService;
    }

    public void renewBy(Backlog backlog) {

        // TODO: fold as set of constraints
        if (hasActiveRenewOfBacklog()) {
            logger.debug("Backlog population skiped, has active renew for: {}", backlog.boundTaskScheme());
            return;
        }

        if (hasActiveTasksFromBacklog(backlog.boundTaskScheme())) {
            logger.debug("Backlog population skiped, has active tasks: {}", backlog.boundTaskScheme());
            return;
        }

        taskIssueService.issueTask(
                BacklogRenewTaskScheme.NAME,
                Collections.singletonMap("schemeName", backlog.boundTaskScheme()),
                "mod-backlog:meta"
        );

    }

    private boolean hasActiveRenewOfBacklog() {

        final long activeCount = taskLogRepository.countByTaskState(
                Task.State.activeStatesList,
                Arrays.asList(
                    new TaskProperty(Task.SCHEME_PROPERTY_NAME, BacklogRenewTaskScheme.NAME)
                )
        );

        return activeCount > 0;
    }

    // TODO: I want to have it as part of backlog definition, various backlogs may
    // TODO: have various constraints for renew
    private boolean hasActiveTasksFromBacklog(String taskSchemeName) {

        final long activeCount = taskLogRepository.countByTaskState(
                Task.State.activeStatesList,
                Arrays.asList(
                    // TODO: this properties are required and installed by core services,
                    // TODO Need some kind of constants or something like this
                    new TaskProperty(Task.SCHEME_PROPERTY_NAME, taskSchemeName),
                    new TaskProperty(Task.CREATOR_PROPERTY_NAME, "mod-backlog")
                )
        );

        return activeCount > 0;
    }

}
