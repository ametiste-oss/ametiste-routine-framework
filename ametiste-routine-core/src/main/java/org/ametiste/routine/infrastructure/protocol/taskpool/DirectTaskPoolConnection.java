package org.ametiste.routine.infrastructure.protocol.taskpool;

import org.ametiste.metrics.annotations.Timeable;
import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.application.service.removing.TaskRemovingService;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.sdk.protocol.taskpool.TaskPoolProtocol;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @since
 */
public class DirectTaskPoolConnection implements TaskPoolProtocol, DirectTaskPoolConnectionMetrics {

    private final String clientId;
    private final TaskIssueService taskIssueService;
    private final TaskRemovingService taskRemovingService;

    public DirectTaskPoolConnection(String clientId,
                                    TaskIssueService taskIssueService,
                                    TaskRemovingService taskRemovingService) {
        this.clientId = clientId;
        this.taskIssueService = taskIssueService;
        this.taskRemovingService = taskRemovingService;
    }

    @Override
    @Timeable(name = OVERAL_ISSUE_TASK_TIMING)
    @Timeable(name = CLIENTS_PREFIX, nameSuffixExpression = CLIENT_ISSUE_TASK_TIMING)
    public UUID issueTask(String taskScheme, Map<String, String> params) {
        return taskIssueService.issueTask(taskScheme, params, clientId);
    }

    @Override
    // TODO: add metrics
    public void removeTasks(List<String> states, Instant afterDate) {
        taskRemovingService.removeTasks(
            states.stream().map(Task.State::valueOf).collect(Collectors.toList()),
            afterDate
        );
    }

    @Override
    public String getClientId() {
        return clientId;
    }

}
