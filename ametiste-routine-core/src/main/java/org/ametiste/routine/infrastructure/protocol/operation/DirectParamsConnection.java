package org.ametiste.routine.infrastructure.protocol.operation;

import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @since
 */
public class DirectParamsConnection implements ParamsProtocol {

    private final String clientId;
    private final UUID operationId;

    public DirectParamsConnection(String clientId, UUID operationId) {
        this.clientId = clientId;
        this.operationId = operationId;
    }

    @Override
    public void fromMap(final Map<String, String> params) {

    }

    @Override
    public Map<String, String> asMap() {
        return null;
    }

    @Override
    public <T extends ParamsProtocol> void proxy(final T params) {

    }

}
