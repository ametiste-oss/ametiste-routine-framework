package org.ametiste.routine.printer.operation;

import org.ametiste.routine.sdk.mod.protocol.ProtocolGateway;
import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.ametiste.routine.sdk.operation.OperationFeedback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.OptionalLong;
import java.util.Random;
import java.util.UUID;

@Component(PrintOperation.NAME)
public final class PrintOperation implements OperationExecutor {

    public static final String NAME = "print-operation";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${org.ametiste.routine.eg.printer.delayTime:1000}")
    private long delyaTime;

    /*
    @Override
    public void execOperation(UUID operationId, Map<String, String> properties, OperationFeedback feedback, ProtocolGateway protocolGateway) {

        final UUID id = protocolGateway.invoke(
                d -> {
                    d.protocol("task-gateway")
                        .message("create-task")
                        .param("task.scheme", "print-eg-task");
                },
                (Map<String, String> m)
                        -> UUID.fromString(m.get("id"))
        );

        protocolGateway.query(q ->
            q.dataSource("mod-data")
                    .select("value")
                    .field("mod-name", "")
                    .field("property-name", "")
                    .accept((String s) -> s.equals("s"))
        );

        final Integer value = protocolGateway.query(
                query -> query.select("value"),
                (String v) -> Integer.valueOf(v)
        );

    } */

    @Override
    public void execOperation(UUID operationId, Map<String, String> properties, OperationFeedback feedback) {

        final long any = new Random().longs(50, delyaTime).findAny().getAsLong();

        feedback.operationNotice("Delay time is: " + any);

        try {
            Thread.sleep(any);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
