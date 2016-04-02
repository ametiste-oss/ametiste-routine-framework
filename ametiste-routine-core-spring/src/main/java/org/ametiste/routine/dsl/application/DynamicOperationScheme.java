package org.ametiste.routine.dsl.application;

import org.ametiste.routine.domain.scheme.OperationScheme;
import org.ametiste.routine.domain.scheme.TaskOperationInstaller;
import org.ametiste.routine.sdk.operation.OperationExecutor;

import java.util.function.Consumer;

public class DynamicOperationScheme implements OperationScheme<DynamicParamsProtocol> {

    private final String operationName;
    private final DynamicOperationFactory opFactory;

    public DynamicOperationScheme(final String operationName, final DynamicOperationFactory opFactory) {
        this.operationName = operationName;
        this.opFactory = opFactory;
    }

    @Override
    public String schemeName() {
        return operationName;
    }

    @Override
    public OperationExecutor operationExecutor() {
        return new DynamicOperationExecutor(opFactory);
    }

    @Override
    public void createOperationFor(final TaskOperationInstaller operationReceiver,
                                   final Consumer<DynamicParamsProtocol> paramsInstaller) {
        operationReceiver.addOperation(operationName, null);
    }

}