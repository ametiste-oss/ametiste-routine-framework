package org.ametiste.lang;

public class Triple<F, S, T> {

    public final F first;
    public final S second;
    public final T third;

    public Triple(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static final <F, S, T> Let<Triple<F, S, T>> let(F first, S second, T third) {
        return c -> c.accept(of(first, second, third));
    }

    public static final <F, S, T> Triple<F, S, T> of(F first, S second, T third) {
        return new Triple<>(first, second, third);
    }

    public F first() {
        return first;
    }

    public S second() {
        return second;
    }

    public T third() { return third; }

}
