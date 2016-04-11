package org.ametiste.routine.dsl.application;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.meta.util.MetaMethodParameter;

import java.lang.annotation.Annotation;
import java.util.Optional;

public abstract class AnnotatedParameterProvider implements ParameterProvider {

    private final Class<? extends Annotation> annotation;

    public AnnotatedParameterProvider(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    @Override
    public final Optional<Object> provideValue(final MetaMethodParameter parameter,
            final ProtocolGateway protocolGateway) {

        final Object value;

        if (match(parameter)) {
            value = resolveParameterValue(parameter, protocolGateway);
        } else {
            value = null;
        }

        return Optional.ofNullable(value);
    }

    private boolean match(final MetaMethodParameter parameter) {
        return parameter.hasAnnotation(annotation);
    }

    abstract protected Object resolveParameterValue(final MetaMethodParameter parameter,
                                                    final ProtocolGateway protocolGateway);

}
