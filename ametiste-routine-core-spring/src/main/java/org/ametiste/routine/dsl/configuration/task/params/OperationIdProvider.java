package org.ametiste.routine.dsl.configuration.task.params;

import org.ametiste.dynamics.foundation.elements.AnnotatedRefProcessor;
import org.ametiste.dynamics.foundation.elements.AnnotatedRef;
import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.dsl.annotations.OperationId;
import org.ametiste.routine.dsl.annotations.ParamValueProvider;
import org.ametiste.routine.dsl.domain.OperationIdAnnotation;
import org.ametiste.routine.sdk.protocol.operation.OperationMetaProtocol;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Provides operation identifier to an element of a task marked by {@link OperationId}.
 * <p>
 * <p> Identifier can be resolved as {@link String} or {@link UUID} valueType according to the
 * {@code valueType} of the annotated parameter. Other paramter types are unsupported and
 * will cause {@link IllegalStateException} at runtime.
 *
 * @implNote This provider using {@link OperationMetaProtocol} to resolve operation meta information.
 * @see OperationId
 * @see OperationIdAnnotation
 * @see OperationMetaProtocol
 * @since 1.1
 */
@Component
@ParamValueProvider
class OperationIdProvider extends AnnotatedRefProcessor<OperationIdAnnotation, Object, ProtocolGateway> {

    public OperationIdProvider() {
        super(OperationIdAnnotation::new);
    }

    @Override
    protected void resolveValue(@NotNull final AnnotatedRef<Object> element, @NotNull final ProtocolGateway protocolGateway) {

        final Object value;
        final UUID operationId = protocolGateway.session(OperationMetaProtocol.class).operationId();

        if (element.isRefeneceTo(UUID.class)) {
            value = operationId;
        } else if (element.isRefeneceTo(String.class)) {
            value = operationId.toString();
        } else {
            throw new IllegalStateException("@OperationId element must have valueType of java.util.UUID or java.lang.String.");
        }

        element.provideValue(value);

    }

}
