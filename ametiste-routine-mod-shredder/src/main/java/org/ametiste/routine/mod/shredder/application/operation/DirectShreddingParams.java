package org.ametiste.routine.mod.shredder.application.operation;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;

/**
 *
 * @since
 */
public class DirectShreddingParams implements ShreddingParams {

    public static final String PARAM_STALE_THRESHOLD_VALUE = "mod-shredding.op.shredding.staleThreshold";

    public static final String PARAM_STALE_THRESHOLD_UNIT = "mod-shredding.op.shredding.staleThresholdUnit";

    public static final String PARAM_STALE_STATES = "mod-shredding.op.shredding.staleStates";

    public static final String PARAM_DISABLE_SESSION_OPTIONS = "mod-shredding.op.shredding.disableSessionOptions";

    public static final List<String> DEFAULT_STALE_STATES = Collections.singletonList(Task.State.DONE.name());

    public static final int DEFAULT_STALE_THRESHOLD_VALUE = 12;

    public static final ChronoUnit DEFAULT_STALE_THRESHOLD_UNIT = ChronoUnit.HOURS;

    public static final boolean DEFAULT_DISABLE_SESSION_OPTIONS = false;

    private final Map<String, String> properties = new HashMap<>();

    @Override
    public Integer threshold() {
        return mayBe(PARAM_STALE_THRESHOLD_VALUE,
                properties, Integer::valueOf, DEFAULT_STALE_THRESHOLD_VALUE);
    }

    @Override
    public ChronoUnit unit() {
        return mayBe(PARAM_STALE_THRESHOLD_UNIT,
                properties, ChronoUnit::valueOf, DEFAULT_STALE_THRESHOLD_UNIT);
    }

    @Override
    public List<String> staleStates() {
        return mayBe(PARAM_STALE_STATES, properties,
                this::splitAsCSList, DEFAULT_STALE_STATES);
    }

    @Override
    public boolean disableSessionOptions() {
        return mayBe(PARAM_DISABLE_SESSION_OPTIONS, properties, Boolean::valueOf, DEFAULT_DISABLE_SESSION_OPTIONS);
    }

    @Override
    public void fromMap(final Map<String, String> params) {
        properties.putAll(params);
    }

    @Override
    public Map<String, String> asMap() {
        return properties;
    }

    @Override
    public <T extends ParamsProtocol> void proxy(final T params) {
        params.fromMap(asMap());
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

    @Override
    public void staleStates(final List<String> staleStates) {
        properties.put(PARAM_STALE_STATES, String.join(",", staleStates));
    }

    @Override
    public void staleThresholdValue(final int staleThresholdValue) {
        properties.put(PARAM_STALE_THRESHOLD_VALUE, Integer.toString(staleThresholdValue));
    }

    @Override
    public void staleThresholdUnit(final ChronoUnit staleThresholdUnit) {
        properties.put(PARAM_STALE_THRESHOLD_UNIT, staleThresholdUnit.name());
    }

    @Override
    public void disableSessionOptions(final boolean disableSessionOptions) {
        properties.put(PARAM_DISABLE_SESSION_OPTIONS, Boolean.toString(disableSessionOptions));
    }

    public static ShreddingParams createFromMap(Map<String, String> params) {
        final ShreddingParams backlogParams = new DirectShreddingParams();
        backlogParams.fromMap(params);
        return backlogParams;
    }

}
