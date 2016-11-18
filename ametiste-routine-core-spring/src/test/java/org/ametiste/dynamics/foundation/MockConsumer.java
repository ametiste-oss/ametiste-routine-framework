package org.ametiste.dynamics.foundation;

import java.util.function.Predicate;

import static org.junit.Assert.assertTrue;

/**
 *
 * @since
 */
public class MockConsumer<T> {

    private final Predicate<T> argPredicate;
    private boolean isCalled;
    private T arg;

    public MockConsumer(final Predicate<T> argPredicate) {
        this.argPredicate = argPredicate;
    }

    public void accept(T arg) {
        this.arg = arg;
        this.isCalled = true;
    }

    public void verify() {
        assertTrue(isCalled);
        assertTrue("Argument predicate is not valid", argPredicate.test(arg));
    }

}
