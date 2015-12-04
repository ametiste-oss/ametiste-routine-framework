package org.ametiste.routine.mod.backlog.infrastructure;

import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.sdk.mod.TaskGateway;

import java.util.Map;

/**
 *
 * @since
 */
public class BacklogTaskGateway implements TaskGateway {

    private final TaskIssueService taskIssueService;

    public BacklogTaskGateway(TaskIssueService taskIssueService) {
        this.taskIssueService = taskIssueService;
    }

    @Override
    public void issueTask(String schemaName, Map<String, String> schemaParams) {
        // TODO: 'mod-backlog' is mod identifier, so we need to centrilize it somehow
        taskIssueService.issueTask(schemaName, schemaParams, "mod-backlog");
    }

}
