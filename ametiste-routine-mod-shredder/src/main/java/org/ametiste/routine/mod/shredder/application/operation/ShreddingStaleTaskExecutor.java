package org.ametiste.routine.mod.shredder.application.operation;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.laplatform.protocol.gateway.SessionOption;
import org.ametiste.routine.mod.shredder.application.schema.ShreddingStaleTaskScheme;
import org.ametiste.routine.mod.shredder.mod.ModShredder;
import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.ametiste.routine.sdk.operation.OperationFeedback;
import org.ametiste.routine.sdk.protocol.modreport.ModReportProtocol;
import org.ametiste.routine.infrastructure.protocol.taskpool.TaskPoolProtocol;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;

/**
 *
 * @since 0.1.0
 */
@Component(ShreddingStaleTaskOperationScheme.NAME)
public class ShreddingStaleTaskExecutor implements OperationExecutor {

    @Override
    public void execOperation(final OperationFeedback feedback, final ProtocolGateway protocolGateway) {

        final Integer threshold = protocolGateway
                .session(ShreddingParams.class)
                .threshold();

        final ChronoUnit unit = protocolGateway
                .session(ShreddingParams.class)
                .unit();

        final List<String> staleStates =protocolGateway
                .session(ShreddingParams.class)
                .staleStates();

        final long removedTasksCount = protocolGateway
                    .session(TaskPoolProtocol.class, SessionOption.STATS)
                    .removeTasks(staleStates, Instant.now().minus(threshold, unit));

        final long lastInvocTime = protocolGateway.sessionOption(TaskPoolProtocol.class, SessionOption.STATS)
                .queryLong("session.last.invocation.time");

        protocolGateway.session(ModReportProtocol.class).submitReport(rb -> {
                rb.type("SHREDDING_STALE_REPORT");
                rb.data("remove.time.taken", Long.toString(lastInvocTime));
                rb.data("remove.tasks.count", Long.toString(removedTasksCount));
        });

    }

    private static <K,V> Optional<V> mayBe(K key, Map<K, V> in) {
        return Optional.ofNullable(in.get(key));
    }

    private static <K, V, T> T mayBe(K key, Map<K, V> in, Function<V, T> as, T else_) {
        return mayBe(key, in).map(as).orElse(else_);
    }

    private List<String> splitAsCSList(String s) {
        return Arrays.asList(s.split(","));
    }

}
