package org.ametiste.routine.printer.operation;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.ametiste.routine.sdk.operation.OperationFeedback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

public final class PrintExecutor implements OperationExecutor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execOperation(OperationFeedback feedback, ProtocolGateway protocolGateway) {

        final long maxDelayTime = protocolGateway
                .session(PrintOperationParams.class).delayTime();

        final String operationOut = protocolGateway
                .session(PrintOperationParams.class).operationOut();

//        final long effectiveDelay = new Random()
//                .longs(50, maxDelayTime).findAny().orElse(0);
//
//        feedback.operationNotice("Delay time is: " + effectiveDelay);

        try {
            Thread.sleep(maxDelayTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
