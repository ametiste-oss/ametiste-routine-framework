package org.ametiste.dynamics.foundation.reflection.structures;

import org.ametiste.dynamics.SurfaceFeature;
import org.ametiste.dynamics.SurfaceStructure;
import org.ametiste.dynamics.foundation.elements.AnnotationSpec;
import org.ametiste.dynamics.foundation.reflection.RuntimePattern;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Represents an object element of a runtime objects graph.
 *
 * @apiNote This class is structural representation of the {@link Object} class in a context
 * of the Surface Model.
 *
 * @see Annotated
 * @since 1.0
 */
@SurfaceStructure(superStructure = ClassStructure.class)
public class ObjectInstance<T> implements Annotated {

    /**
     * A reference on the {@link Object} that represented by this structure.
     *
     * @apiNote This field is accessable for the package members
     * to provide easy access for internals. Feel free to use it inside the package.
     */
    final T object;

    private final ClassStructure<T> classStructure;

    public ObjectInstance(final @NotNull T object,
                          final @NotNull ClassStructure<T> classStructure) {
        this.object = object;
        this.classStructure = classStructure;
    }

    /**
     * Returns a type of this object.
     *
     * @return a type of this object, can't be null
     * @since 1.0
     */
    @NotNull
    @SurfaceFeature
    public Class<T> type() {
        return classStructure.type();
    }

    /**
     * Matches fields of an object by the given matcher, provides a result of matching
     * to the given consumer as an {@link ObjectInstanceField}.
     * <p>See {@link ClassMethod#parameters(UnaryOperator)} for matchers explanations.
     *
     * @apiNote This method represents a calculation over structure, each invocation and each consumer
     * will receive their own instance of each found {@link ObjectInstanceField}.
     *
     * @param matcher  an operator over pattern that represents fields matcher, must be not null
     * @param consumer a consumer of a matched fields, must be not null
     * @since 1.0
     */
    @SurfaceFeature
    public void fields(final @NotNull UnaryOperator<RuntimePattern> matcher,
                       final @NotNull Consumer<ObjectInstanceField<?>> consumer) {
        classStructure.fields(matcher,
                classField -> consumer.accept(new ObjectInstanceField<>(classField, this))
        );
    }

    /**
     * Provides fields of this object to the given consumer one by one.
     *
     * @apiNote This method represents a calculation over structure, each invocation and each consumer
     * will receive their own instance of each found {@link ObjectInstanceField}.
     *
     * @param matcher  an operator over pattern that represents fields matcher, must be not null
     * @param consumer a consumer of a matched fields, must be not null
     * @since 1.0
     */
    @SurfaceFeature
    public void fields(final @NotNull Consumer<ObjectInstanceField<?>> consumer) {
        classStructure.fields(
            classField -> consumer.accept(new ObjectInstanceField<>(classField, this))
        );
    }

    /**
     * Represents the given class method as a method of an object that represented by this
     * structure and provides it the given consumer as an {@link ObjectInstanceMethod}.
     *
     * @apiNote This method represents a calculation over structure, each invocation and each consumer
     * will receive their own instance of {@link ObjectInstanceMethod} for each method found.
     *
     * @implNote This implementation does not any check for that this class method
     * is belongs to this object, so if there is no such method of the object any attempt to proceed
     * to invokation will produce {@link java.lang.reflect.InvocationTargetException}.
     *
     * @param method a class method to be represented as the object method, must be not null
     * @param consumer a consumer of a method, must be not null
     *
     * @since 1.0
     */
    @SurfaceFeature
    public void method(final @NotNull ClassMethod method,
                       final @NotNull Consumer<ObjectInstanceMethod> consumer) {
        consumer.accept(
            new ObjectInstanceMethod(this, method)
        );
    }

    /**
     * @since 1.0
     */
    @NotNull
    @Override
    public <T extends AnnotationSpec> T annotation(final @NotNull Function<AnnotatedDescriptor, T> value) {
        return classStructure.annotation(value);
    }

    /**
     * @since 1.0
     */
    @Override
    public boolean hasAnnotations(final @NotNull Function<AnnotatedDescriptor, ? extends AnnotationSpec> handler) {
        return classStructure.hasAnnotations(handler);
    }

}
