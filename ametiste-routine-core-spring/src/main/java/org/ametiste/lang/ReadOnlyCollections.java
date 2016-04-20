package org.ametiste.lang;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 *
 * @since
 */
public class ReadOnlyCollections {

    public static <T> List<T> readOnlyList(final List<T> elements) {
        return Collections.unmodifiableList(new ArrayList<>(elements));
    }

    public static <T> List<T> readOnlyList(final T ...elements) {
        return Collections.unmodifiableList(Arrays.asList(elements));
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

    public static <V> Map<Class<?>, V> readOnlyMapOfClassObjects(final V ...entries) {
        return readOnlyMap(Stream.of(entries).map(ReadOnlyCollections::classAsKey));
    }

    public static <V> Pair<Class<?>, V> classAsKey(final V value) {
        return Pair.of(value.getClass(), value);
    }

}
