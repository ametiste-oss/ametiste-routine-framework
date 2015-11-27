package org.ametiste.routine.printer.operation;

import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.ametiste.routine.sdk.operation.OperationExecutorFactory;
import org.springframework.stereotype.Component;

@Component(PrintOperation.NAME)
public class PrintOperationExecutorFactory implements OperationExecutorFactory {

    @Override
    public OperationExecutor createExecutor() {
        return new PrintOperation();
    }

}
