package org.ametiste.lang;

import java.util.function.BiConsumer;

/**
 *
 * @since
 */
public interface BiLet<T, U> {

    void let(BiConsumer<T, U> consumer);

    static <T, U> void let(final T first, final U second, final BiConsumer<T, U> consumer) {
        if (first != null && second != null) {
            consumer.accept(first, second);
        }
    }

}
