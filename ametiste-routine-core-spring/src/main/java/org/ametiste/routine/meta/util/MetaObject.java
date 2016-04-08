package org.ametiste.routine.meta.util;

import org.ametiste.lang.Pair;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 *
 * @since
 */
public class MetaObject {

    // NOTE: accessable by MetaMethod
    final Object object;

    private final Class<?> klass;

    public MetaObject(Object object) {

        if (object == null) {
            throw new IllegalArgumentException("Given object is null.");
        }

        this.object = object;
        this.klass = object.getClass();
    }

    public long countAnnotatedMethod(Class<? extends Annotation> annotationClass) {
        return Stream.of(klass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(annotationClass))
                .count();
    }

    public void assertTypeAnnotation(Class<? extends Annotation> annotationClass) {
        if (!klass.isAnnotationPresent(annotationClass)) {
            throw new IllegalStateException("@" + annotationClass.getSimpleName() + " is required to be declared for "
                    + klass.getName());
        }
    }

    public Optional<MetaMethod> oneAnnotatedMethod(Class<? extends Annotation> annotationClass) {

        if (countAnnotatedMethod(annotationClass) != 1) {
            return Optional.empty();
        }

        return Stream.of(klass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(annotationClass))
                .map(m -> MetaMethod.of(this, m))
                .findFirst();
    }

    public <T, S extends Annotation> Optional<T> annotationValue(
            Class<S> annotationClass, Function<S, T> valueProducer) {

        if (!klass.isAnnotationPresent(annotationClass)) {
            return Optional.empty();
        }

        return Optional.ofNullable(valueProducer.apply(klass.getDeclaredAnnotation(annotationClass)));
    }

    public Stream<Field> streamOfAnnotatedFields(final Class<? extends Annotation> annotationClass) {
        return Stream.of(klass.getDeclaredFields()).filter(
                f -> f.isAnnotationPresent(annotationClass)
        );
    }

    public Stream<MetaMethod> streamOfMethodsWithoutAnnotation(final Class<? extends Annotation> annotationClass) {
        return Stream.of(klass.getMethods()).filter(
            f -> !f.isAnnotationPresent(annotationClass)
        ).map(m -> MetaMethod.of(this, m));
    }

    public Stream<MetaMethod> streamOfAnnotatedMethods(final Class<? extends Annotation> annotationClass) {
        return Stream.of(klass.getMethods()).filter(
                f -> f.isAnnotationPresent(annotationClass)
        ).map(m -> MetaMethod.of(this, m));
    }

    public Object object() {
        return object;
    }

    public static final MetaObject of(Object object) {
        return new MetaObject(object);
    }

    public static final MetaObject from(Class<?> klass) {
        try {
            return MetaObject.of(klass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

}
