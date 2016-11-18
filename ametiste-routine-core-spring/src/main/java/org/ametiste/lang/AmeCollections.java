package org.ametiste.lang;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 *
 * @since
 */
public class AmeCollections {

    public static <T> List<T> mergeUnique(final List<T> first, final List<T> second) {
        final List<T> merged = merge(first, second);
        final List<T> unique = new ArrayList<>(merged.size());
        merged.stream().filter(a -> !unique.contains(a)).forEach(unique::add);
        return readOnlyList(unique);
    }

    public static <T> List<T> merge(final List<T> first, final List<T> second) {
        final List<T> merged = new ArrayList<>(first);
        merged.addAll(second);
        return readOnlyList(merged);
    }

    public static <T> List<T> readOnlyList(final List<T> other) {
        return Collections.unmodifiableList(new ArrayList<>(other));
    }

    public static <T> List<T> readOnlyList(final T ...elements) {
        return Collections.unmodifiableList(Arrays.asList(elements));
    }

    public static <K,V> Map<K, V> readOnlyMap(final Consumer<Map<K, V>> builder) {
        final HashMap<K, V> source = new HashMap<>();
        builder.accept(source);
        return readOnlyMap(source);
    }

    public static <K,V> Map<K, V> readOnlyMap(final Map<K, V> other) {
        return Collections.unmodifiableMap(new HashMap<>(other));
    }

    public static <K,V> Map<K, V> readOnlyMap(final Stream<Pair<K, V>> entries) {
        return entries.collect(toMap(p -> p.first, p -> p.second));
    }

    public static <K,V> Map<K, V> readOnlyMap(final List<Pair<K, V>> entries) {
        return readOnlyMap(entries.stream());
    }

    public static <K,V> Map<K, V> readOnlyMap(final Pair<K, V> ...entries) {
        return readOnlyMap(Stream.of(entries));
    }

    public static <K, V> Pair<K, V> entry(K key, V value) {
        return Pair.of(key, value);
    }

    public static <V> Map<Class<?>, V> readOnlyMapOfClassObjects(final V ...entries) {
        return readOnlyMap(Stream.of(entries).map(AmeCollections::classAsKey));
    }

    public static <V> Pair<Class<?>, V> classAsKey(final V value) {
        return Pair.of(value.getClass(), value);
    }

}
