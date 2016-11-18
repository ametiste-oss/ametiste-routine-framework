package org.ametiste.lang;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 *
 * @since
 */
public interface BiElective<T, U> {

    void let(BiConsumer<T, U> consumer);

    static <T, U> void let(BiPredicate<T, U> predicate,
                           final Supplier<T> first,
                           final Supplier<U> second,
                           final BiConsumer<T, U> consumer) {
        if (predicate.test(first.get(), second.get())) {
            consumer.accept(first.get(), second.get());
        }
    }

    static <T, U> void let(final T first, final U second, final BiConsumer<T, U> consumer) {
        let((f,s) -> f != null && s != null, () -> first, () -> second, consumer);
    }

}
