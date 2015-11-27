package org.ametiste.routine.mod.backlog.application.service;

import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.mod.backlog.application.scheme.BacklogRenewTaskScheme;
import org.ametiste.routine.mod.backlog.domain.Backlog;
import org.ametiste.routine.mod.tasklog.domain.TaskLogRepository;

import java.util.Collections;

/**
 *
 * @since 0.1.0
 */
public class BacklogRenewService {

    private final TaskLogRepository taskLogRepository;

    private final TaskIssueService taskIssueService;

    public BacklogRenewService(TaskLogRepository taskLogRepository, TaskIssueService taskIssueService) {
        this.taskLogRepository = taskLogRepository;
        this.taskIssueService = taskIssueService;
    }

    public void renewBy(Backlog backlog) {

        if (hasActiveTasksFromBacklog(backlog.boundTaskScheme())) {
            return;
        }

        taskIssueService.issueTask(
                BacklogRenewTaskScheme.NAME,
                Collections.singletonMap("schemeName", backlog.boundTaskScheme()),
                "mod-backlog"
        );

    }

    // TODO: I want to have it as part of backlog definition, various backlogs may
    // TODO: have various constraints for renew
    private boolean hasActiveTasksFromBacklog(String taskSchemeName) {

        final int activeCount = taskLogRepository.countByTaskState(
                Task.State.activeStates,
                new TaskProperty[]{
                    // TODO: this properties are required and installed by core services,
                    // TODO Need some kind of constants or something like this
                    new TaskProperty("task.scheme", taskSchemeName),
                    new TaskProperty("created.by", "mod-backlog")
                }
        );

        return activeCount > 0;
    }

}
