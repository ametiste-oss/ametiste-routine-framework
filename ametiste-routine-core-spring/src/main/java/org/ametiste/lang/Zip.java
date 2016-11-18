package org.ametiste.lang;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @since
 */
public final class Zip {

    private Zip() { }

    public static final <A, B> Stream<Pair<A,B>> zip(Stream<A> as, Stream<B> bs) {
        Iterator<A> i1 = as.iterator();
        Iterator<B> i2 = bs.iterator();
        Iterable<Pair<A,B>> i=()->new Iterator<Pair<A,B>>() {
            public boolean hasNext() {
                return i1.hasNext() && i2.hasNext();
            }
            public Pair<A,B> next() {
                return new Pair<A,B>(i1.next(), i2.next());
            }
        };
        return StreamSupport.stream(i.spliterator(), false);
    }

    public static final <T, U> boolean constraint(final List<T> first,
                                                  final List<U> second,
                                                  final BiPredicate<T, U>...criteria) {
        final BiPredicate<T, U> predicate = Stream.of(criteria).reduce((t, o) -> true, BiPredicate::and);
        return zip(first.stream(), second.stream()).allMatch(p -> predicate.test(p.first, p.second));
    }

    public static final <T, U> boolean isIsomorph(final List<T> first,
                                                  final List<U> second,
                                                  final BiPredicate<T, U>...homs) {

        if (first.size() != second.size()) {
            return false;
        }

        if (homs.length == 0) {
            return false;
        }

        final BiPredicate<T, U> homPredicate = Stream.of(homs)
                .reduce((t, o) -> true, BiPredicate::and);

        final Predicate<T> predicateOverFirst = second.stream().map(
                s -> (Predicate<T>) (T f) -> homPredicate.test(f, s)
        ).reduce(f -> false, Predicate::or);

        return first.stream().filter(predicateOverFirst).count() == first.size();
    }


}
