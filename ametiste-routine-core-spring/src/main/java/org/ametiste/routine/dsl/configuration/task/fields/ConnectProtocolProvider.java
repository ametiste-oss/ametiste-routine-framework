package org.ametiste.routine.dsl.configuration.task.fields;

import org.ametiste.dynamics.foundation.feature.ReferenceFeature;
import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.laplatform.sdk.protocol.Protocol;
import org.ametiste.routine.dsl.annotations.Connect;
import org.ametiste.routine.dsl.annotations.FieldValueProvider;
import org.ametiste.dynamics.foundation.AnnotatedElementValueProvider;
import org.ametiste.dynamics.SurfaceElement;

/**
 * <p>
 *     Connects the operation field marked as {@link Connect} to the protocol, type of connection depicted by a
 *     field type.
 * </p>
 *
 * <p>
 *     This provider is using runtime instance of {@link ProtocolGateway} to resolve connection instance.
 * </p>
 *
 * <p>
 *     Field marked by the annotation, must have type that extends {@link Protocol} type.
 *     Other field types are unsupported and will cause an {@link IllegalStateException} at a runtime.
 * </p>
 *
 * @see Connect
 * @see ProtocolGateway
 * @see Protocol
 * @since 1.1
 */
// TODO: I want to extract this provider to the ame-la-platform
@FieldValueProvider
class ConnectProtocolProvider extends AnnotatedElementValueProvider<ProtocolGateway> {

    public ConnectProtocolProvider() {
        super(Connect.class);
    }

    @Override
    protected Object resolveValue(final SurfaceElement element,
                                  final ProtocolGateway protocolGateway) {
        // TODO: add IllegalStateException specified by the class javadoc
        // TODO: add exception to protocol gateway, if @Connect does not point on LambdaProtocol element
        return protocolGateway.session(element.mapFeature(ReferenceFeature::type, ReferenceFeature.class));
    }

}
