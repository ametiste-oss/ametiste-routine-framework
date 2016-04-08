package org.ametiste.routine.meta.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Function;

/**
 *
 * @since
 */
public class MetaMethod {

    private final MetaObject metaObject;
    private final Method method;

    public MetaMethod(MetaObject metaObject, Method method) {
        this.metaObject = metaObject;
        this.method = method;
    }

    public static final MetaMethod of(MetaObject metaObject, Method method) {
        return new MetaMethod(metaObject, method);
    }

    public MetaObject ofClass() {
        return metaObject;
    }

    public void invoke(Object... args) {
        try {
            method.invoke(metaObject.object, args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public MetaMethod assertParametersCount(final int paramsCount) {

        if (method.getParameterCount() != paramsCount) {
            throw new IllegalStateException(metaObject.object.getClass().getName() + "#" + method.getName() +
                    " is expected to have parameters number of: " + paramsCount);
        }

        return this;
    }

    // TODO: copypaste from MetaObject, may we have it as general?
    public <T, S extends Annotation> Optional<T> annotationValue(
            Class<S> annotationClass, Function<S, T> valueProducer) {

        if (!method.isAnnotationPresent(annotationClass)) {
            return Optional.empty();
        }

        return Optional.ofNullable(valueProducer.apply(method.getDeclaredAnnotation(annotationClass)));
    }

    public MetaMethod assertAttributesTypes(final Class<?>... expectedTypes) {

        final Class<?>[] actualTypes = method.getParameterTypes();

        if (actualTypes.length != expectedTypes.length) {
            throw new IllegalStateException(metaObject.object.getClass().getName() + "#" + method.getName() +
                    " is expected to have " + expectedTypes.length + " parameters.");
        }

        for (int i = 0; i < expectedTypes.length; i++) {
            if (!actualTypes[i].equals(expectedTypes[i])) {
                throw new IllegalStateException(metaObject.object.getClass().getName() + "#" + method.getName() +
                    " is expected to have parameter #" + i + " of type " + expectedTypes[i] + " but " + actualTypes[i] + " given.");
            }
        }

        return this;
    }

    public String name() {
        return method.getName();
    }
}
