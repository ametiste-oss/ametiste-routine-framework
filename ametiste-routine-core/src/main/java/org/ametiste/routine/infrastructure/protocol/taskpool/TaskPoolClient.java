package org.ametiste.routine.infrastructure.protocol.taskpool;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.sdk.mod.TaskGateway;

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
        throw new UnsupportedOperationException("Deprecated API. Please use ProtocolGateway directly.");
    }

}
