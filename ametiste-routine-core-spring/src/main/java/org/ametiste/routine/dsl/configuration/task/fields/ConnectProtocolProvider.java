package org.ametiste.routine.dsl.configuration.task.fields;

import org.ametiste.dynamics.foundation.elements.AnnotatedRefProcessor;
import org.ametiste.dynamics.foundation.elements.AnnotatedRef;
import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.laplatform.sdk.protocol.Protocol;
import org.ametiste.routine.dsl.annotations.Connect;
import org.ametiste.routine.dsl.annotations.FieldValueProvider;
import org.ametiste.routine.dsl.domain.ConnectAnnotation;
import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * Connects the operation field marked as {@link Connect} to the protocol, type of connection depicted by a
 * field type.
 * </p>
 * <p>
 * <p>
 * This provider is using runtime instance of {@link ProtocolGateway} to resolve connection instance.
 * </p>
 * <p>
 * <p>
 * Field marked by the annotation, must have type that extends {@link Protocol} type.
 * Other field types are unsupported and will cause an {@link IllegalStateException} at a runtime.
 * </p>
 *
 * @see Connect
 * @see ConnectAnnotation
 * @see ProtocolGateway
 * @see Protocol
 * @since 1.1
 */
// TODO: I want to extract this provider to the ame-la-platform
@FieldValueProvider
class ConnectProtocolProvider extends AnnotatedRefProcessor<ConnectAnnotation, Protocol, ProtocolGateway> {

    public ConnectProtocolProvider() {
        super(ConnectAnnotation::new);
    }

    @Override
    protected void resolveValue(@NotNull final AnnotatedRef<Protocol> element, @NotNull final ProtocolGateway protocolGateway) {
        element.provideValue(protocolGateway.session(element.type()));
    }

}
