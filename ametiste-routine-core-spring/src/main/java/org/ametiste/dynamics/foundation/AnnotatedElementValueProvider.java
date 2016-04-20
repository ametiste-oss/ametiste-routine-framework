package org.ametiste.dynamics.foundation;

import org.ametiste.dynamics.SurfaceElement;
import org.ametiste.dynamics.DynamicValueProvider;

import java.lang.annotation.Annotation;
import java.util.Optional;

public abstract class AnnotatedElementValueProvider<C> implements DynamicValueProvider<C> {

    private final Class<? extends Annotation> annotation;

    public AnnotatedElementValueProvider(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    @Override
    public final Optional<Object> provideValue(final SurfaceElement element,
                                               final C protocolGateway) {

        final Object value;

        if (match(element)) {
            value = resolveValue(element, protocolGateway);
        } else {
            value = null;
        }

        return Optional.ofNullable(value);
    }

    private boolean match(final SurfaceElement element) {
        return element.isAnnotatedBy(annotation);
    }

    abstract protected Object resolveValue(final SurfaceElement element, final C protocolGateway);

}
