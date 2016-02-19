package org.ametiste.routine.mod.backlog.application.operation;

import org.ametiste.routine.sdk.protocol.operation.OperationProtocol;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @since
 */
public class BacklogParams implements OperationProtocol {

    private final Map<String, String> params;

    public BacklogParams(Map<String, String> params) {
        this.params = params;
    }

    public String schemeName() {
        return params.get("schemeName");
    }

    @Override
    public UUID operationId() {
        return null;
    }

}
