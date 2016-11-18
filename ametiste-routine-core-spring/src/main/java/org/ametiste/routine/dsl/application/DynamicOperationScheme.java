package org.ametiste.routine.dsl.application;

import org.ametiste.routine.domain.scheme.OperationScheme;
import org.ametiste.routine.domain.scheme.TaskOperationInstaller;
import org.ametiste.routine.dsl.domain.surface.RoutineOperationStructure;
import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class DynamicOperationScheme implements OperationScheme<DynamicParamsProtocol> {

    @NotNull
    private final RoutineOperationStructure operationStructure;

    public DynamicOperationScheme(@NotNull final RoutineOperationStructure operationStructure) {
        this.operationStructure = operationStructure;
//        this.operationName = operationName;
//        // TODO: may be construct invocation in the context? Here I just can have some data to show and invocation
//        this.invocation = invocation;
    }

    @NotNull
    @Override
    public String schemeName() {
        return operationStructure.name();
    }

    @NotNull
    @Override
    public OperationExecutor operationExecutor() {
        return (feedback, gateway) -> operationStructure.invoke(gateway);
    }

    @Override
    public void createOperationFor(@NotNull final TaskOperationInstaller operationReceiver,
                                   @NotNull final Consumer<DynamicParamsProtocol> paramsInstaller) {
        operationReceiver.addOperation(operationStructure.name(), null);
    }

}