package org.ametiste.dynamics;

import org.ametiste.lang.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 *
 * @since
 */
public class InitializationData {

    public final static InitializationData EMPTY = new InitializationData(Collections.emptyMap(),
            Collections.emptyMap());

    private final Map<Field, Object> fieldValues = new HashMap<>();
    private final Map<Method, Object[]> methodParams = new HashMap<>();

    public InitializationData(final Pair<Field, Object>[] fieldValues, final Pair<Method, Object[]>[] methodParams) {
        Stream.of(fieldValues).forEach(p -> p.asMapEntryFirstKey(this.fieldValues));
        Stream.of(methodParams).forEach(p -> p.asMapEntryFirstKey(this.methodParams));
    }

    public InitializationData(final Map<Field, Object> fieldValues, final Map<Method, Object[]> methodParams) {
        this.fieldValues.putAll(fieldValues);
        this.methodParams.putAll(methodParams);
    }

    public Optional<Object> fieldValue(Field field) {
        return Optional.ofNullable(fieldValues.get(field));
    }

    public Optional<Object[]> methodParams(Method method) {
        return Optional.ofNullable(methodParams.get(method));
    }

    public InitializationData merge(InitializationData other) {
        return new InitializationData(
                extend(this.fieldValues, other.fieldValues),
                extend(this.methodParams, other.methodParams)
        );
    }

    private <K,V> Map<K, V> extend(final Map<K, V> origin, final Map<K, V> extension) {
        final Map<K, V> extended = new HashMap<>(origin);
        extended.putAll(extension);
        return Collections.unmodifiableMap(extended);
    }

    public static InitializationData ofPair(final Pair<Method, Object[]> pair) {
        return new InitializationData(Collections.emptyMap(), Collections.singletonMap(pair.first, pair.second));
    }

    public static InitializationData of(final Method method, final Object[] params) {
        return new InitializationData(Collections.emptyMap(), Collections.singletonMap(method, params));
    }

    public static InitializationData empty() {
        return EMPTY;
    }
}
