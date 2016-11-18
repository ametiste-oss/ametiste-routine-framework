package org.ametiste.dynamics.foundation;

import java.util.function.Predicate;

import static org.junit.Assert.assertTrue;

/**
 *
 * @since
 */
public class MockFunction<T, R> {

    private final Predicate<T> argPredicate;
    private final R mockResult;
    private boolean isApplied;
    private T arg;

    public MockFunction(final Predicate<T> argPredicate, final R mockResult) {
        this.argPredicate = argPredicate;
        this.mockResult = mockResult;
    }

    public R apply(T arg) {
        if (isApplied) {
            throw new IllegalStateException("This mock function is already applied, please create new mock consumer.");
        }
        this.arg = arg;
        this.isApplied = true;
        return mockResult;
    }

    public void verify() {
        assertTrue(isApplied);
        assertTrue("Argument predicate is not valid", argPredicate.test(arg));
    }

}
