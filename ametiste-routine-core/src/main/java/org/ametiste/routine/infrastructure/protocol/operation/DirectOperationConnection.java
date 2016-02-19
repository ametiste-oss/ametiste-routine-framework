package org.ametiste.routine.infrastructure.protocol.operation;

import org.ametiste.routine.domain.ModReportRepository;
import org.ametiste.routine.sdk.protocol.operation.OperationProtocol;

import java.util.UUID;

/**
 *
 * @since
 */
public class DirectOperationConnection implements OperationProtocol {

    private final String clientId;
    private final UUID operationId;

    public DirectOperationConnection(String clientId, UUID operationId) {
        this.clientId = clientId;
        this.operationId = operationId;
    }

    @Override
    public UUID operationId() {
        return operationId;
    }

}
