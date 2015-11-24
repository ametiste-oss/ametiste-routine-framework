package org.ametiste.routine.example.app.printer.operation;

import org.ametiste.routine.sdk.application.service.execution.OperationExecutor;
import org.ametiste.routine.sdk.application.service.execution.OperationExecutorFactory;
import org.springframework.stereotype.Component;

@Component(PrintOperation.NAME)
public class PrintOperationExecutorFactory implements OperationExecutorFactory {

    @Override
    public OperationExecutor createExecutor() {
        return new PrintOperation();
    }

}
