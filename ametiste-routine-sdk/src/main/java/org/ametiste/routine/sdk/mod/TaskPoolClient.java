package org.ametiste.routine.sdk.mod;

import org.ametiste.routine.sdk.mod.protocol.ProtocolGateway;

import java.util.Map;

import static org.ametiste.routine.sdk.mod.TaskPool.Message.*;

/**
 *
 * @since
 */
public class TaskPoolClient implements TaskGateway {

    private final ProtocolGateway gateway;

    public TaskPoolClient(ProtocolGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void issueTask(String schemaName, Map<String, String> schemaParams) {
        gateway.session(p -> p.protocol(TaskPool.PROTOCOL_NAME)
            .message(IssueTask.TYPE)
            .param(IssueTask.Param.TASK_SCHEME, schemaName)
            .param(IssueTask.Param.TASK_SCHEME_PARAMS, schemaParams)
        );
    }

}
