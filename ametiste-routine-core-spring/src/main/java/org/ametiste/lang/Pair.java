package org.ametiste.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class Pair<F, S> implements Elective<Pair<F, S>>, Transformable<Pair<F, S>> {

    public final F first;
    public final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public static final <F, S> Pair<F, S> of(F first, S second) {
        return new Pair<>(first, second);
    }

    public static <T, U> List<Pair<T, U>> of(List<T> firsts, List<U> seconds) {

        if (firsts.size() != seconds.size()) {
            throw new IllegalArgumentException("Lists to pair must have an equal size!");
        }

        List<Pair<T, U>> pairs = new ArrayList<Pair<T, U>>();
        for (int i = 0; i < firsts.size(); i++) {
            pairs.add(Pair.of(firsts.get(i), seconds.get(i)));
        }

        return pairs;
    }

    public F first() {
        return first;
    }

    public S second() {
        return second;
    }

    public void asMapEntryFirstKey(final Map<F, S> map) {
        map.put(first, second);
    }

    public void asMapEntrySecondKey(final Map<S, F> map) {
        map.put(second, first);
    }

    @Override
    public void let(final Consumer<Pair<F, S>> block) {
        block.accept(this);
    }

    @Override
    public <R> R map(final Function<Pair<F, S>, R> transformation) {
        return transformation.apply(this);
    }

}
