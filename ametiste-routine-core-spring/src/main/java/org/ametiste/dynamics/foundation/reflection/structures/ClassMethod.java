package org.ametiste.dynamics.foundation.reflection.structures;

import org.ametiste.dynamics.SurfaceFeature;
import org.ametiste.dynamics.SurfaceStructure;
import org.ametiste.dynamics.foundation.elements.AnnotationSpec;
import org.ametiste.dynamics.foundation.reflection.RuntimePattern;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Represents a method element of a runtime objects graph, that belongs to a class.
 *
 * @apiNote This class is structural representation of the {@link Method} class in a context
 * of the Surface Model.
 * @since 1.0
 */
@SurfaceStructure(superStructure = ClassStructure.class)
public class ClassMethod implements Annotated {

    private final Method method;
    private final ClassStructure owner;
    private final AnnotatedDescriptorDelegate descriptor;

    public ClassMethod(final @NotNull ClassStructure<?> owner, final @NotNull Method method) {
        this.owner = owner;
        this.method = method;
        this.descriptor = new AnnotatedDescriptorDelegate(method::isAnnotationPresent, method::getAnnotation);
    }

    /**
     * Returns a qualified name of a method, a name that contains a qualified name of an owner class.
     * <p>The qualified name have a format like <i>org.domain.package.ClassName#methodName</i>.
     *
     * @return a qualified name of a method, can't be null
     * @since 1.0
     */
    @SurfaceFeature
    @NotNull
    public String qualifiedName() {
        return owner.qualifiedName() + "#" + name();
    }

    /**
     * Returns a name of a method.
     *
     * @return a name of a method, can't be null
     * @since 1.0
     */
    @SurfaceFeature
    @NotNull
    public String name() {
        return method.getName();
    }

    /**
     * Matches a paramters of a method by the given matcher, maps each found parameter by
     * the given mapper as {@link MethodParameter}, and invokes the method using these parameters.
     *
     * <p>The matcher is the {@link UnaryOperator} over {@link RuntimePattern}, that used
     * to specify parameter features to be used for matching.
     * <p>Example of usage
     * <code>
     * <pre>
     * method.invoke(
     *    object,
     *    pattern -> pattern.hasAnnotations(SomeAnnotation.class),
     *    parameter -> parameter.referenceTo("New Value")
     * )
     * </pre>
     * </code>
     *
     * @apiNote
     * <p>If there is a need to map each parameter just pas empty pattern <i>p -> p</i>.
     * <p>This method represents a calculation over structure, each invocation
     * will produce new instances of a found {@link MethodParameter}s.
     *
     * @param instance an instance that is target of invocation, must be not null
     * @param matcher an operator over pattern that represents paramteres matcher, must be not null
     * @param mapper a function to map each found parameter
     *
     * @see MethodParameter
     * @see RuntimePattern
     * @since 1.0
     */
    @SurfaceFeature
    public void invoke(final @NotNull ObjectInstance<?> instance,
                       final @NotNull UnaryOperator<RuntimePattern> matcher,
                       final @NotNull UnaryOperator<MethodParameter<Object>> mapper) {

        final Object[] params = RuntimePattern.create(matcher).map(annotations ->
            Stream.of(method.getParameters())
                    .filter(p -> annotations.stream().allMatch(p::isAnnotationPresent))
                    .map(m -> new MethodParameter<>(this, m)))
            .map(mapper)
            .map(MethodParameter::value)
            .toArray();

        try {
            method.invoke(instance.object, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO: custom exception
            throw new RuntimeException(e);
        }
    }

    /**
     * @since 1.0
     */
    @SurfaceFeature
    @Override
    @NotNull
    public <T extends AnnotationSpec> T annotation(final @NotNull Function<AnnotatedDescriptor, T> value) {
        return value.apply(descriptor);
    }

    /**
     * @since 1.0
     */
    @SurfaceFeature
    @Override
    public boolean hasAnnotations(final @NotNull Function<AnnotatedDescriptor, ? extends AnnotationSpec> handler) {
        return descriptor.hasAnnotations(handler.apply(descriptor).annotation());
    }

}