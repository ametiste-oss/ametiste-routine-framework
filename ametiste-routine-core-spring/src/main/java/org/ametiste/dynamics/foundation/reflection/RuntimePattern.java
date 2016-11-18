package org.ametiste.dynamics.foundation.reflection;

import org.ametiste.dynamics.foundation.reflection.structures.ClassField;
import org.ametiste.lang.BiElective;
import org.ametiste.lang.BiTransformable;
import org.ametiste.lang.Elective;
import org.ametiste.lang.Transformable;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.function.*;

import static org.ametiste.lang.AmeCollections.mergeUnique;
import static org.ametiste.lang.AmeCollections.readOnlyList;

/**
 * Value object that describes some runtime element from the point of view of features
 * of this element.
 * <p>For example, it allow to describe the element that contains a set of defined annotations.
 * <p>This object primarily used to specify various lookup queries to the {@link DynamicsRuntime}
 * elements.
 * <p> Note, a pattern without any rule is the empty pattern, such patterns will match nothing.
 *
 * @implSpec Any client that uses pattern to match structures should match nothing in case where
 * the given pattern is empty.
 *
 * @apiNote This class described by {@link Elective} and {@link Transformable} types that are provides
 * convenient access methods.
 *
 * @since 1.0
 * @see DynamicsRuntime
 */
public final class RuntimePattern implements
        Elective<List<Class<? extends Annotation>>>,
        Transformable<List<Class<? extends Annotation>>> {

    public static final RuntimePattern EMPTY = new RuntimePattern();

    private final List<Class<? extends Annotation>> annotations;

    private RuntimePattern(final @NotNull List<Class<? extends Annotation>> annotations) {
        this.annotations = readOnlyList(annotations);
    }

    private RuntimePattern() {
        this(readOnlyList());
    }

    /**
     * Creates new runtime pattern from the empty one using the given matcher.
     *
     * @param matcher an operator to provide pattern to be used to create new pattern, must be not null
     * @return new pattern that created by the given matcher, can't be null
     */
    @NotNull
    public static final RuntimePattern create(final @NotNull UnaryOperator<RuntimePattern> matcher) {
        return matcher.apply(EMPTY);
    }

    /**
     * Adds the given annotations to this pattern and returns new pattern that contains these annotations.
     *
     * @param annotations set of annotations, may be empty, but elements must be not null
     * @return new runtime pattern that match object which has all annotations
     * from the given annotations set, can't be null
     */
    @NotNull
    public final RuntimePattern hasAnnotations(final @NotNull Class<? extends Annotation>... annotations) {
        return new RuntimePattern(mergeUnique(this.annotations, Arrays.asList(annotations)));
    }

    /**
     * Invokes the given consumer block if the list of <i>annotations</i> is not null and not empty. The given consumer
     * block will receive a list of annotations as an argument of invocation.
     *
     * @since 1.0
     */
    @Override
    public final void let(final @NotNull Consumer<List<Class<? extends Annotation>>> block) {
        Elective.let(a -> a != null && !a.isEmpty(), annotations, block);
    }

    /**
     * @since 1.0
     */
    @NotNull
    @Override
    public final <R> R map(final @NotNull Function<List<Class<? extends Annotation>>, R> transformation) {
        return transformation.apply(annotations);
    }

}
