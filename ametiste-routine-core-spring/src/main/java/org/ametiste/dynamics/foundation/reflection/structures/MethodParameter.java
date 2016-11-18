package org.ametiste.dynamics.foundation.reflection.structures;

import org.ametiste.dynamics.SurfaceStructure;
import org.ametiste.dynamics.foundation.elements.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Parameter;
import java.util.function.Function;

/**
 * Represents a method parameter.
 *
 * @param <T> a type of parameter
 * @apiNote This class is structural representation of the {@link Parameter} class in a context
 * of the Surface Model.
 * @see Reference
 * @see Annotated
 * @since 1.0
 */
@SurfaceStructure(superStructure = ClassMethod.class)
public class MethodParameter<T> implements Reference<T>, Annotated {

    private final ClassMethod methodStructure;
    private final Parameter parameter;
    private final AnnotatedDescriptorDelegate rawAnnotated;
    private T ref;

    public MethodParameter(final @NotNull ClassMethod methodStructure,
                           final @NotNull Parameter parameter) {
        this.parameter = parameter;
        this.methodStructure = methodStructure;
        this.rawAnnotated = new AnnotatedDescriptorDelegate(parameter::isAnnotationPresent, parameter::getAnnotation);
    }

    /**
     * @since 1.0
     */
    @Override
    public boolean ofType(final @NotNull Class<?> type) {
        return type.isAssignableFrom(parameter.getType());
    }

    /**
     * @since 1.0
     */
    @NotNull
    @Override
    public Class<T> type() {
        return (Class<T>) parameter.getType();
    }

    /**
     * @since 1.0
     */
    @Override
    public void referencesTo(final @NotNull T ref) {
        this.ref = ref;
    }

    /**
     * Returns a value of this paramter.
     *
     * @return a value bound to this parameter, if any, can be null.
     * @since 1.0
     */
    // TODO: may be it should be optional?
    @NotNull
    public T value() {
        return ref;
    }

    /**
     * @since 1.0
     */
    @NotNull
    @Override
    public <T extends AnnotationSpec> T annotation(final @NotNull Function<AnnotatedDescriptor, T> value) {
        return value.apply(rawAnnotated);
    }

    /**
     * @since 1.0
     */
    @Override
    public boolean hasAnnotations(final @NotNull Function<AnnotatedDescriptor, ? extends AnnotationSpec> handler) {
        return rawAnnotated.hasAnnotations(handler.apply(rawAnnotated).annotation());
    }

}