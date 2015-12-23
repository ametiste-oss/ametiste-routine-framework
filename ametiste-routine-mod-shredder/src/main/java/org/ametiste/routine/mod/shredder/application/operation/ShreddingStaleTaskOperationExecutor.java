package org.ametiste.routine.mod.shredder.application.operation;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.mod.shredder.mod.ModShredder;
import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.ametiste.routine.sdk.operation.OperationFeedback;
import org.ametiste.routine.sdk.protocol.taskpool.TaskPoolProtocol;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @since 0.1.0
 */
@Component(ShreddingStaleTaskOperationExecutor.NAME)
public class ShreddingStaleTaskOperationExecutor implements OperationExecutor {

    public static final String NAME = ModShredder.MOD_ID + "::shreddingStaleOperation";

    public static final String PARAM_STALE_THRESHOLD_VALUE = "mod-shredding.op.shredding.staleThreshold";

    public static final String PARAM_STALE_THRESHOLD_UNIT = "mod-shredding.op.shredding.staleThresholdUnit";

    public static final int DEFAULT_STALE_THRESHOLD_VALUE = 30;

    public static final ChronoUnit DEFAULT_STALE_THRESHOLD_UNIT = ChronoUnit.HOURS;

    @Override
    public void execOperation(final UUID operationId, final Map<String, String> properties,
                          final OperationFeedback feedback, final ProtocolGateway protocolGateway) {

        final Integer threshold = mayBe(PARAM_STALE_THRESHOLD_VALUE,
                properties, Integer::valueOf, DEFAULT_STALE_THRESHOLD_VALUE);

        final ChronoUnit unit = mayBe(PARAM_STALE_THRESHOLD_UNIT,
                properties, ChronoUnit::valueOf, DEFAULT_STALE_THRESHOLD_UNIT);

        protocolGateway.session(TaskPoolProtocol.class).removeTasks(
            Collections.singletonList("DONE"),
            Instant.now().minus(threshold, unit)
        );
    }

    private static <K,V> Optional<V> mayBe(K key, Map<K, V> in) {
        return Optional.ofNullable(in.get(key));
    }

    private static <K, V, T> T mayBe(K key, Map<K, V> in, Function<V, T> as, T else_) {
        return mayBe(key, in).map(as).orElse(else_);
    }

}
