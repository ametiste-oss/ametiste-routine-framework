package org.ametiste.routine.mod.shredder.application.operation;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.laplatform.protocol.gateway.SessionOption;
import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.ametiste.routine.sdk.operation.OperationFeedback;
import org.ametiste.routine.sdk.protocol.modreport.ModReportProtocol;
import org.ametiste.routine.infrastructure.protocol.taskpool.TaskPoolProtocol;
import org.ametiste.routine.sdk.protocol.modreport.ReportForm;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @since 0.1.0
 */
public class ShreddingStaleTaskExecutor implements OperationExecutor {

    @Override
    public void execOperation(final OperationFeedback feedback, final ProtocolGateway protocolGateway) {

        final ShreddingParams shreddingParams = protocolGateway
                .session(ShreddingParams.class);

        final Integer threshold =  shreddingParams
                .threshold();

        final ChronoUnit unit = shreddingParams
                .unit();

        final List<String> staleStates = shreddingParams
                .staleStates();

        final List<SessionOption> sessionOptions = resolveSessionOptions(shreddingParams);

        final long removedTasksCount = protocolGateway
                    .session(TaskPoolProtocol.class, sessionOptions)
                    .removeTasks(staleStates, Instant.now().minus(threshold, unit));


        Consumer<ReportForm> reportBuilder = rb -> {
            rb.type("SHREDDING_STALE_REPORT");
            rb.data("remove.tasks.count", Long.toString(removedTasksCount));
        };

        if (sessionOptions.contains(SessionOption.STATS)) {
            final long lastInvocTime = protocolGateway
                    .sessionOption(TaskPoolProtocol.class, SessionOption.STATS)
                    .queryLong("session.last.invocation.time");

            reportBuilder = reportBuilder.andThen(rb -> rb.data("remove.time.taken", Long.toString(lastInvocTime)));
        }

        protocolGateway.session(ModReportProtocol.class).submitReport(reportBuilder);

    }

    private static <K,V> Optional<V> mayBe(K key, Map<K, V> in) {
        return Optional.ofNullable(in.get(key));
    }

    private static <K, V, T> T mayBe(K key, Map<K, V> in, Function<V, T> as, T else_) {
        return mayBe(key, in).map(as).orElse(else_);
    }

    private static List<SessionOption> resolveSessionOptions(ShreddingParams params) {
        return params.disableSessionOptions() ? Collections.emptyList() : Collections.singletonList(SessionOption.STATS);
    }

    private List<String> splitAsCSList(String s) {
        return Arrays.asList(s.split(","));
    }

}
