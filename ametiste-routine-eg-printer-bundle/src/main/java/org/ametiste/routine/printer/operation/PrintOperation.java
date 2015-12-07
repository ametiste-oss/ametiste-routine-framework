package org.ametiste.routine.printer.operation;

import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.ametiste.routine.sdk.operation.OperationFeedback;

import java.util.Map;
import java.util.UUID;

public final class PrintOperation implements OperationExecutor {

    public static final String NAME = "print-operation";

    @Override
    public void execOperation(UUID operationId, Map<String, String> properties, OperationFeedback feedback) {
        try {
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
