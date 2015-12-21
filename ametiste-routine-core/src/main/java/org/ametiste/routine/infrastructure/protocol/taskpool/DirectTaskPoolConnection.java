package org.ametiste.routine.infrastructure.protocol.taskpool;

import org.ametiste.metrics.annotations.Timeable;
import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.sdk.protocol.taskpool.TaskPoolProtocol;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @since
 */
public class DirectTaskPoolConnection implements TaskPoolProtocol, DirectTaskPoolConnectionMetrics {

    private final String clientId;
    private final TaskIssueService taskIssueService;

    public DirectTaskPoolConnection(String clientId, TaskIssueService taskIssueService) {
        this.clientId = clientId;
        this.taskIssueService = taskIssueService;
    }

    @Override
    @Timeable(name = OVERAL_ISSUE_TASK_TIMING)
    @Timeable(name = CLIENTS_PREFIX, nameSuffixExpression = CLIENT_ISSUE_TASK_TIMING)
    public UUID issueTask(String taskScheme, Map<String, String> params) {
        return taskIssueService.issueTask(taskScheme, params, clientId);
    }

    @Override
    public String getClientId() {
        return clientId;
    }
}
