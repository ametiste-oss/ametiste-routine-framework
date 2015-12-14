package org.ametiste.routine.infrastructure.protocol.taskpool;

import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.sdk.mod.TaskPoolProtocol;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @since
 */
public class DirectTaskPoolProtocol implements TaskPoolProtocol {

    private final String creatorIdentifier;
    private final TaskIssueService taskIssueService;

    public DirectTaskPoolProtocol(String creatorIdentifier, TaskIssueService taskIssueService) {
        this.creatorIdentifier = creatorIdentifier;
        this.taskIssueService = taskIssueService;
    }

    @Override
    public UUID issueTask(String taskScheme, Map<String, String> params) {
        return taskIssueService.issueTask(taskScheme, params, creatorIdentifier);
    }
}
