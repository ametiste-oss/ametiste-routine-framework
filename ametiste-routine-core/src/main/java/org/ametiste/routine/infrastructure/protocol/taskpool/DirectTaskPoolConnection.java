package org.ametiste.routine.infrastructure.protocol.taskpool;

import org.ametiste.metrics.annotations.Timeable;
import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.application.service.removing.TaskRemovingService;
import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
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
    public <T extends ParamsProtocol> UUID issueTask(final Class<? extends TaskScheme<T>>
                                                        taskScheme, final Consumer<T> paramsInstaller) {
        return taskIssueService.issueTask(taskScheme, paramsInstaller, clientId);
    }

    @Override
    @Timeable(name = OVERAL_REMOVE_TASKS_TIMING)
    @Timeable(name = CLIENTS_PREFIX, nameSuffixExpression = CLIENT_REMOVE_TASKS_TIMING)
    public long removeTasks(List<String> states, Instant afterDate) {
        return taskRemovingService.removeTasks(
            states.stream().map(Task.State::valueOf).collect(Collectors.toList()),
            afterDate,
            clientId
        );
    }

    @Override
    public String getClientId() {
        return clientId;
    }

}
