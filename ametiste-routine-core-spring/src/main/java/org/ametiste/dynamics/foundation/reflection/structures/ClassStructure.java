package org.ametiste.dynamics.foundation.reflection.structures;

import org.ametiste.dynamics.Surface;
import org.ametiste.dynamics.SurfaceFeature;
import org.ametiste.dynamics.SurfaceStructure;
import org.ametiste.dynamics.Surge;
import org.ametiste.dynamics.foundation.elements.AnnotationSpec;
import org.ametiste.dynamics.foundation.reflection.RuntimePattern;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Represents a class element of a runtime objects graph.
 *
 * @param <T> a type that this class defines
 * @apiNote This class is structural representation of the {@link Class} class in a context
 * of the Surface Model.
 * @since 1.0
 */
@SurfaceStructure(superStructure = ClassPool.class)
public class ClassStructure<T> implements Annotated {

    private final Class<T> klass;
    private final AnnotatedDescriptorDelegate descriptor;

    public ClassStructure(final @NotNull Class<T> klass) {
        this.klass = klass;
        this.descriptor = new AnnotatedDescriptorDelegate(klass::isAnnotationPresent, klass::getAnnotation);
    }

    /**
     * Matches fields of a class by the given matcher, provides a result of matching
     * to the given consumer as a {@link ClassField}.
     * <p>
     * <p>See {@link ClassMethod#parameters(UnaryOperator)} for matchers explanations.
     *
     * @apiNote This method represents a calculation over structure, each invocation and each consumer
     * will receive their own instance of each found {@link ClassField}.
     *
     * @param matcher  an operator over pattern that represents fields matcher, must be not null
     * @param consumer a consumer of a matched fields, must be not null
     * @since 1.0
     */
    @SurfaceFeature
    public void fields(final @NotNull UnaryOperator<RuntimePattern> matcher,
                       final @NotNull Consumer<ClassField> consumer) {
        RuntimePattern.create(matcher).let(annotations ->
            Stream.of(klass.getDeclaredFields())
                    .filter(p -> annotations.stream().allMatch(p::isAnnotationPresent))
                    .map(f -> new ClassField(this, f))
                    .forEach(consumer)
        );
    }


    /**
     * Provides fields of this class to the given consumer one by one.
     *
     * @apiNote This method represents a calculation over structure, each invocation and each consumer
     * will receive their own instance of each found {@link ClassField}.
     *
     * @param consumer a consumer of a matched fields, must be not null
     * @since 1.0
     */
    @SurfaceFeature
    public void fields(final @NotNull Consumer<ClassField> consumer) {
        Stream.of(klass.getDeclaredFields())
            .map(f -> new ClassField(this, f))
            .forEach(consumer);
    }

    /**
     * Transforms methods of a class that matched by the given matcher, appliyng the
     * given mapper function for each method, and provides a result of mapping
     * to the given consumer in the order defined by the given {@link Comparator} defined over
     * type that is result of the transformation.
     * <p>See {@link ClassMethod#parameters(UnaryOperator)} for matchers explanations.
     * <p>Example of method usage, this code matches methods that have <i>TaskOperation</i> annotation
     * and maps them to <i>RoutineOperationStructure</i> in order defined by <i>this::operationsOrder</i>
     * method that implements comporator over <i>RoutineOperationStructure</i>.
     * <code>
     * <pre>
     *  klass.mapMethods(
     *      method -> method.hasAnnotations(TaskOperation.class),
     *      RoutineOperationStructure::new,
     *      this::operationsOrder
     *  );
     * </pre>
     * </code>
     *
     * @apiNote Since this method represents a calculation over structure, each invocation
     * will produce a new stream of a found {@link ClassMethod}s.
     *
     * @param <T> a type of transformation result
     * @param matcher  an operator over pattern that represents fields matcher, must be not null
     * @param consumer a consumer of a matched fields, must be not null
     * @param order a comporator that defines order of mapping, must be not null
     *
     * @return ordered stream of defined mapping operation, can't be null
     * @since 1.0
     */
    @NotNull
    @SurfaceFeature
    public <T> Stream<T> mapMethods(final @NotNull UnaryOperator<RuntimePattern> matcher,
                                    final @NotNull Function<ClassMethod, T> mapper,
                                    final @NotNull Comparator<T> order) {
        return mapMethods(matcher, mapper, s -> s.sorted(order));
    }

    /**
     * Transforms methods of a class that matched by the given matcher, appliyng the
     * given mapper function for each method, and applies the given enchance to a stream of results
     * of this mapping.
     * <p>The enchance operator may be used to affect the stream of results in some way, for example
     * ordering for this stream may be achieved by the using of <i>stream -> stream.ordered(comporator)</i>
     * echance.
     * <p>See {@link ClassMethod#parameters(UnaryOperator)} for matchers explanations.
     * <p>Example of method usage, this code matches methods that have <i>TaskOperation</i> annotation
     * and maps them to <i>RoutineOperationStructure</i> in order defined by <i>this::operationsOrder</i>
     * method that implements comporator over <i>RoutineOperationStructure</i>.
     * <code>
     * <pre>
     *  klass.mapMethods(
     *      method -> method.hasAnnotations(TaskOperation.class),
     *      RoutineOperationStructure::new,
     *      operation -> this.operationsOrder(operation)
     *  );
     * </pre>
     * </code>
     *
     * @apiNote Since this method represents a calculation over structure, each invocation
     * will produce a new stream of a found {@link ClassMethod}s.
     *
     * @param <T> a type of transformation result
     * @param matcher  an operator over pattern that represents fields matcher, must be not null
     * @param consumer a consumer of a matched fields, must be not null
     *
     * @since 1.0
     */
    @NotNull
    @SurfaceFeature
    public <T> Stream<T> mapMethods(final @NotNull UnaryOperator<RuntimePattern> matcher,
                                    final @NotNull Function<ClassMethod, T> mapper,
                                    final @NotNull UnaryOperator<Stream<T>> enhance) {
        return RuntimePattern.create(matcher).map(annotations ->
                enhance.apply(Stream.of(klass.getDeclaredMethods())
                        .filter(m -> annotations.stream().allMatch(m::isAnnotationPresent))
                        .map(m -> new ClassMethod(this, m))
                        .map(mapper)
                )
        );
    }

    /**
     * Returns a qualified name of a class.
     * <p>The qualified name have a format like <i>org.domain.package.ClassName</i>.
     *
     * @return a qualified name of a class, can't be null
     * @since 1.0
     */
    @NotNull
    @SurfaceFeature
    public String qualifiedName() {
        return klass.getName();
    }

    /**
     * Returns a type of a class.
     *
     * @return a type of a class, can't be null.
     */
    @NotNull
    @SurfaceFeature
    public Class<T> type() {
        return klass;
    }

    /**
     * @since 1.0
     */
    @NotNull
    @Override
    public <T extends AnnotationSpec> T annotation(final @NotNull Function<AnnotatedDescriptor, T> value) {
        return value.apply(descriptor);
    }

    /**
     * @since 1.0
     */
    @Override
    public boolean hasAnnotations(final @NotNull Function<AnnotatedDescriptor, ? extends AnnotationSpec> handler) {
        try {
            return descriptor.hasAnnotations(handler.apply(descriptor).annotation());
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Creates new instance of a class that described by this structure. Created instance than provided to the
     * given consumer.
     *
     * <p>The given list of {@link Surge}s will be applied to a {@link Surface}s that formed by features of fields
     * of the new instance.
     *
     * @apiNote The list of surges is mechanics to extend process of an instance creation, for example if there is
     * a customization that have need to provide custom values of fields, this list is the right
     * place to start such customization.
     * <p> At the moment fields explosion mechanics
     * is implemented at the place, so there is a context of explosion. But in a future this will be moved
     * to a {@link org.ametiste.dynamics.Planar} implementation that will provide such capabilities.
     *
     * @implNote Each surface of a field of a new instance that passed to the given list of surges will have
     * type of the {@link org.ametiste.dynamics.RightSurface} typed as {@link ObjectInstanceField}.
     *
     * @param surges a list of surges to explode a new instance fields
     * @param context a context of instantination ( primarily used to explode fields, see api note )
     * @param consumer a consumer for a new instance
     * @param <C> a type of instantination context
     *
     * @since 1.0
     */
    @SurfaceFeature
    public <C> void newInstance(final @NotNull List<? extends Surge> surges,
                                final @NotNull C context,
                                final @NotNull Consumer<ObjectInstance<T>> consumer) {

        final ObjectInstance<T> object;

        try {
            object = new ObjectInstance<>(klass.newInstance(), this);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        object.fields(field -> surges.forEach(s -> s.explode(Surface.rightSurface(field), context)));

        consumer.accept(object);

    }

}
