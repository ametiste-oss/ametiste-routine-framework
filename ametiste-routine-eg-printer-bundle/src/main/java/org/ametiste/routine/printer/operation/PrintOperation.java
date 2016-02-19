package org.ametiste.routine.printer.operation;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.ametiste.routine.sdk.operation.OperationFeedback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Component(PrintOperation.NAME)
public final class PrintOperation implements OperationExecutor {

    public static final String NAME = "print-operation";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${org.ametiste.routine.eg.printer.delayTime:1000}")
    private long delyaTime;

    @Override
    public void execOperation(OperationFeedback feedback, ProtocolGateway protocolGateway) {

        final long any = new Random().longs(50, delyaTime).findAny().getAsLong();

        feedback.operationNotice("Delay time is: " + any);

        try {
            Thread.sleep(any);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
