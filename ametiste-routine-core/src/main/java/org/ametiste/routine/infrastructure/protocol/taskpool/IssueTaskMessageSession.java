package org.ametiste.routine.infrastructure.protocol.taskpool;

import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.sdk.mod.TaskPool;
import org.ametiste.routine.sdk.mod.protocol.GatewayCallback;
import org.ametiste.routine.sdk.mod.protocol.GatewayResponseMapper;
import org.ametiste.routine.sdk.mod.protocol.ProtocolMessage;
import org.ametiste.routine.sdk.mod.protocol.MessageSession;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @since
 */
@ProtocolMessage(TaskPool.Message.IssueTask.class)
public class IssueTaskMessageSession implements MessageSession {

    private final TaskIssueService taskIssueService;

    private Map<String, Map<String, String>> params;

    private GatewayCallback callback;

    public IssueTaskMessageSession(TaskIssueService taskIssueService) {
        this.taskIssueService = taskIssueService;
    }

    @Override
    public void params(Map<String, Map<String, String>> params) {
        this.params = params;
    }

    @Override
    public void callback(GatewayCallback callback) {
        this.callback = callback;
    }

    @Override
    public <T> T collect(GatewayResponseMapper<T> collector) {
        return null;
    }

    @Override
    public void end() {

        final UUID issueTask = taskIssueService.issueTask(
                params.get(TaskPool.Message.IssueTask.Param.TASK_SCHEME).get("value"),
                params.get(TaskPool.Message.IssueTask.Param.TASK_SCHEME_PARAMS),
                params.get("creatorId").get("value")
        );

        this.callback.call(
            Collections.singletonMap(TaskPool.Message.IssueTask.Response.ISSUED_TASK_ID, issueTask.toString())
        );

    }

}
