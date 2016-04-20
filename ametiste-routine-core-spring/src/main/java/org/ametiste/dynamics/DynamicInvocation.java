package org.ametiste.dynamics;

import org.ametiste.lang.Pair;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class DynamicInvocation<T, C> {

    private final Supplier<T> constructor;

    private final Method method;
    private final List<DynamicInitializer<T, C>> initializers;

    public DynamicInvocation(Supplier<T> constructor, Method method, DynamicInitializer<T, C> ...initializers) {
        this(constructor, method, Arrays.asList(initializers));
    }

    public DynamicInvocation(Supplier<T> constructor, Method method, List<DynamicInitializer<T, C>> initializers) {
        this.constructor = constructor;
        this.method = method;
        this.initializers = initializers;
    }

    public Runnable asRunnable(C context) {
        return prepareInvokePair(context).map(this::runnableInvoke);
    }

    public void invoke(C context) {
        prepareInvokePair(context).let(this::invokeSafe);
    }

    private Pair<T, Object[]> prepareInvokePair(final C context) {
        final T instance = createInstance();
        return initializers.stream().map(init -> init.apply(instance, context))
                .reduce(InitializationData::merge)
                .flatMap(d -> d.methodParams(method))
                .map(p -> Pair.of(instance, p))
                .orElseThrow(() -> new RuntimeException("Can't prepare invocation in the given context."));
    }

    private Object invokeSafe(final Pair<T, Object[]> instanceParams) {
        try {
            return method.invoke(instanceParams.first, instanceParams.second);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Runnable runnableInvoke(final Pair<T, Object[]> instanceParams) {
        return () -> invokeSafe(instanceParams);
    }

    private T createInstance() {
        try {
            return constructor.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}