package org.ametiste.lang;

public class Pair<F, S> {

    public final F first;
    public final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public static final <F, S> Pair<F, S> of(F first, S second) {
        return new Pair<>(first, second);
    }

    public F first() {
        return first;
    }

    public S second() {
        return second;
    }
}
