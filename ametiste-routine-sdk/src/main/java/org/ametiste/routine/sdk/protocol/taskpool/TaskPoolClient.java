package org.ametiste.routine.sdk.protocol.taskpool;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.sdk.mod.TaskGateway;
import org.ametiste.routine.sdk.protocol.taskpool.TaskPoolProtocol;

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
        gateway.session(TaskPoolProtocol.class)
                .issueTask(schemaName, schemaParams);
    }

}
