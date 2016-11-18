package org.ametiste.dynamics.foundation.reflection.structures;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Class that implements {@link AnnotatedDescriptor} behavior and provides
 * convenient interface to build object that provides such description.
 * <p>
 * <p>This class is constructed using two functions, the first is for the {@link AnnotatedDescriptor#hasAnnotations(Class[])},
 * and the second is for the {@link AnnotatedDescriptor#annotationValue(Function, Class)} methods.
 * <p>The functions that used to construct the delegate is devided on two groups - the group of
 * <i>implementing</i> functions and the group of <i>matching</i> functions.
 * <p>The functions from the <i>implementing</i> group are direct
 * implementations for methods, these functions are that mimics methods signatures.
 * <p>The functions from the <i>matching</i> group are simplifications
 * that implements only "matching" part of methods.
 * <p>Example of usage in a context of some {@link ClassField} class using <i>implementing</i> functions
 * <code>
 * <pre>
 * public class ClassField implements Annotated {
 *
 *      private final AnnotatedDescriptorDelegate descriptor;
 *
 *      public ClassField(final ClassStructure<?> classStructure, final Field field) {
 *          ...
 *          this.descriptor = new AnnotatedDescriptorDelegate(
 *              annotations -> annotations.allMatch(field::isAnnotationPresent),
 *              (annotation, transform) -> Optional.ofNullable(field.getAnnotation(annotation)).map(transform)
 *          );
 *          ...
 *      }
 *  ...
 * }
 * </pre>
 * </code>
 * <p>Example of usage in a context of some {@link ClassField} class using <i>matching</i> functions
 * <code>
 * <pre>
 * public class ClassField implements Annotated {
 *
 *      private final AnnotatedDescriptorDelegate descriptor;
 *
 *      public ClassField(final ClassStructure<?> classStructure, final Field field) {
 *          ...
 *          this.descriptor = new AnnotatedDescriptorDelegate(field::isAnnotationPresent, field::getAnnotation);
 *          ...
 *      }
 *  ...
 * }
 * </pre>
 * </code>
 * @apiNote This class is internal for this package and used only to implement {@link AnnotatedDescriptor} interface
 * for classes of the package.
 * @see AnnotatedDescriptor
 * @since 1.0
 */
class AnnotatedDescriptorDelegate implements AnnotatedDescriptor {

    /**
     * A predicate function that represents
     * implementation of the {@link AnnotatedDescriptor#hasAnnotations(Class[])} method
     */
    private final Predicate<Stream<Class<? extends Annotation>>> hasAnnotations;

    /**
     * A function that represents
     * implementation of the {@link AnnotatedDescriptor#annotationValue(Function, Class)} method
     */
    private final BiFunction<Class<? extends Annotation>, Function, Optional> annotationValue;

    /**
     * Constructs new delegate that implements the interface using the given <i>implementing</i> functions.
     *
     * @param hasAnnotations  a function to implement {@link #hasAnnotations(Class[])} method, must be not null
     * @param annotationValue a function to implement {@link #annotationValue(Function, Class)} method, must be not null
     *
     * @since 1.0
     */
    public AnnotatedDescriptorDelegate(final @NotNull Predicate<Stream<Class<? extends Annotation>>> hasAnnotations,
                                       final @NotNull BiFunction<Class<? extends Annotation>, Function, Optional> annotationValue) {
        this.hasAnnotations = hasAnnotations;
        this.annotationValue = annotationValue;
    }

    /**
     * Constructs new delegate that implements the interface using the given <i>matching</i> functions.
     *
     * @param isAnnotationPresent a predicate to match that an annotation is present on an element
     * @param extractAnnotation a function to extract an annotation of an element
     *
     * @since 1.0
     */
    public AnnotatedDescriptorDelegate(final @NotNull Predicate<Class<? extends Annotation>> isAnnotationPresent,
                                       final @NotNull Function<Class<? extends Annotation>, Annotation> extractAnnotation) {
        this(
            annotations -> annotations.allMatch(isAnnotationPresent),
            (annotation, transform) -> Optional.ofNullable(extractAnnotation.apply(annotation)).map(transform)
        );
    }

    /**
     * @since 1.0
     */
    @Override
    public boolean hasAnnotations(final @NotNull Class<? extends Annotation>... annotations) {
        return hasAnnotations.test(Stream.of(annotations));
    }

    /**
     * @since 1.0
     */
    @NotNull
    @Override
    public <T, A extends Annotation> Optional<T> annotationValue(final @NotNull Function<A, T> value,
                                                                 final @NotNull Class<A> annotation) {
        return annotationValue.apply(annotation, value);
    }

}
