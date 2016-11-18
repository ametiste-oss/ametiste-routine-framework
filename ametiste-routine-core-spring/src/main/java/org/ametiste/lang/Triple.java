package org.ametiste.lang;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Triple<F, S, T> {

    public final F first;
    public final S second;
    public final T third;

    public Triple(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public final <R> R map(final Function<Triple<F,S,T>, R> transform) {
        return transform.apply(this);
    }

    public final <R> R mapFirstSecond(final BiFunction<F, S, R> transform) {
        return transform.apply(first, second);
    }

    public final <R> R mapFirstThird(final BiFunction<F, T, R> transform) {
        return transform.apply(first, third);
    }

    public final <R> R mapSecondThird(final BiFunction<S, T, R> transform) {
        return transform.apply(second, third);
    }

    public final <R> R mapSecondFirst(final BiFunction<S, F, R> transform) {
        return transform.apply(second, first);
    }

    public final <R> R mapThirdFirst(final BiFunction<T, F, R> transform) {
        return transform.apply(third, first);
    }

    public final <R> R mapThirdSecond(final BiFunction<T, S, R> transform) {
        return transform.apply(third, second);
    }

    public F first() {
        return first;
    }

    public S second() {
        return second;
    }

    public T third() { return third; }

    public static final <F, S, T> Elective<Triple<F, S, T>> let(F first, S second, T third) {
        return c -> c.accept(of(first, second, third));
    }

    public static final <F, S, T> Triple<F, S, T> of(F first, S second, T third) {
        return new Triple<>(first, second, third);
    }

    static class Cat {
        private final String name;
        private final Integer size;

        public Cat(String name, Integer size) {
            this.name = name;
            this.size = size;
        }
    }

    static class Dog {
        private final String name;
        private final Integer size;

        public Dog(String name, Integer size) {
            this.name = name;
            this.size = size;
        }
    }

}
