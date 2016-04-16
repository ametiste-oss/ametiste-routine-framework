package org.ametiste.routine.meta.util;

import org.ametiste.routine.meta.util.MetaMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.function.Function;

/**
 *
 * @since
 */
public class MetaMethodParameter {

    private final MetaMethod metaMethod;
    private final Parameter parameter;

    MetaMethodParameter(MetaMethod metaMethod, Parameter parameter) {
        this.metaMethod = metaMethod;
        this.parameter = parameter;
    }

    public boolean hasAnnotation(final Class<? extends Annotation> annotation) {
        return parameter.isAnnotationPresent(annotation);
    }

    // TODO: copypaste from MetaObject, may we have it as general?
    public <T, S extends Annotation> Optional<T> optionalAnnotationValue(
            Class<S> annotationClass, Function<S, T> valueProducer) {

        if (!parameter.isAnnotationPresent(annotationClass)) {
            return Optional.empty();
        }

        return Optional.ofNullable(valueProducer.apply(parameter.getDeclaredAnnotation(annotationClass)));
    }

    public <T, S extends Annotation> T annotationValue(
            Class<S> annotationClass, Function<S, T> valueProducer) {
        return optionalAnnotationValue(annotationClass, valueProducer).get();
    }

    public Class<?> type() {
        return parameter.getType();
    }

    public Parameter self() { return parameter; }

    public String name() {
        return parameter.getName();
    }
}
