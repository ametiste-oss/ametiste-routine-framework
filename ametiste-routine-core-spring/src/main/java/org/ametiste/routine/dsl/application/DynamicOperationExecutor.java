package org.ametiste.routine.dsl.application;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.ametiste.routine.sdk.operation.OperationFeedback;

class DynamicOperationExecutor implements OperationExecutor {

    private final DynamicOperationFactory operationFactory;

    public DynamicOperationExecutor(final DynamicOperationFactory operationFactory) {
        this.operationFactory = operationFactory;
    }

    @Override
    public void execOperation(final OperationFeedback feedback, final ProtocolGateway protocolGateway) {
        operationFactory.createDynamicOperation(feedback, protocolGateway).run();
    }

}