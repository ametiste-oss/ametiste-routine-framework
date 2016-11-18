package org.ametiste.dynamics.foundation;

import org.ametiste.dynamics.foundation.reflection.structures.ClassPoolTest;

import java.util.function.Predicate;

/**
 *
 * @since
 */
public class MockFuncs {

    public static final <T> MockConsumer<T> consumer() {
        return new MockConsumer<>(p -> true);
    }

    public static final <T> MockConsumer<T> consumer(final Predicate<T> argPredicate) {
        return new MockConsumer<>(argPredicate);
    }

    public static final <T, R> MockFunction<T, R> function(final R mockResult) {
        return new MockFunction<>(p -> true, mockResult);
    }

    public static final <T, R> MockFunction<T, R> function(final Predicate<T> argPredicate, final R mockResult) {
        return new MockFunction<>(argPredicate, mockResult);
    }

}
