package org.ametiste.routine.printer.operation;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.ametiste.routine.sdk.operation.OperationFeedback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component(PrintOperationScheme.NAME)
public final class PrintOperation implements OperationExecutor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${org.ametiste.routine.eg.printer.delayTime:1000}")
    private long delyaTime;

    @Override
    public void execOperation(OperationFeedback feedback, ProtocolGateway protocolGateway) {

        final long any = new Random().longs(50, delyaTime).findAny().orElse(0);

        final String operationOut = protocolGateway
                .session(PrintOperationParams.class).operationOut();

        feedback.operationNotice("Delay time is: " + any);

        try {
            Thread.sleep(any);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (logger.isTraceEnabled()) {
            logger.trace("Operation out is: " + operationOut);
        }

    }

}
