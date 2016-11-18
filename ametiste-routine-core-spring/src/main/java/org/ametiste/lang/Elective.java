package org.ametiste.lang;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface Elective<T> {

    void let(Consumer<T> block);

    static <T> void let(final Predicate<T> predicate, final T value, final Consumer<T> consumer) {
        if (predicate.test(value)) {
            consumer.accept(value);
        }
    }

    static <T> void let(final T value, final Consumer<T> consumer) {
        let(v -> v != null, value, consumer);
    }

}
