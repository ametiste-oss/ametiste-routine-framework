package org.ametiste.routine.dsl.application;

import org.ametiste.laplatform.protocol.ProtocolGateway;

import java.lang.annotation.Annotation;
import java.util.Optional;

public abstract class AnnotatedElementValueProvider implements RuntimeElementValueProvider {

    private final Class<? extends Annotation> annotation;

    public AnnotatedElementValueProvider(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    @Override
    public final Optional<Object> provideValue(final RuntimeElement element,
                                               final ProtocolGateway protocolGateway) {

        final Object value;

        if (match(element)) {
            value = resolveValue(element, protocolGateway);
        } else {
            value = null;
        }

        return Optional.ofNullable(value);
    }

    private boolean match(final RuntimeElement element) {
        return element.isAnnotatedBy(annotation);
    }

    abstract protected Object resolveValue(final RuntimeElement element, final ProtocolGateway protocolGateway);

}
