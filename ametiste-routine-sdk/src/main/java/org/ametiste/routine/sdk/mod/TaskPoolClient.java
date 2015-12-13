package org.ametiste.routine.sdk.mod;

import org.ametiste.routine.sdk.mod.protocol.ProtocolGateway;

import java.util.Map;

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
        gateway.invoke(
                p -> p.protocol("task-gateway")
                        .message("issue-task")
                        .param("task.scheme", schemaName)
                        .param("scheme.params", schemaParams)
        );
    }

}
