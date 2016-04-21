package org.ametiste.routine.dsl.configuration.task.params;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.dsl.annotations.OperationId;
import org.ametiste.routine.dsl.annotations.ParamValueProvider;
import org.ametiste.dynamics.foundation.AnnotatedElementValueProvider;
import org.ametiste.dynamics.SurfaceElement;
import org.ametiste.routine.sdk.protocol.operation.OperationMetaProtocol;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.ametiste.dynamics.foundation.structure.ReflectFoundation.referenceTo;

/**
 * <p>
 *     Resolves operation id for parameter marked as {@link OperationId}.
 * </p>
 *
 * <p>
 *     This provider using {@link OperationMetaProtocol} to resolve operation meta information.
 * </p>
 *
 * <p>
 *     Identifier can be resolved as {@link String} or {@link UUID} valueType according to the
 *     valueType of the annotated parameter. Other paramter types are unsupported and
 *     will cause {@link IllegalStateException} at runtime.
 * </p>
 *
 * @see OperationId
 * @see OperationMetaProtocol
 * @since 1.1
 */
@Component
@ParamValueProvider
class OperationIdProvider extends AnnotatedElementValueProvider<ProtocolGateway> {

    public OperationIdProvider() {
        super(OperationId.class);
    }

    @Override
    protected Object resolveValue(final SurfaceElement element,
                                  final ProtocolGateway protocolGateway) {

        final Object value;
        final UUID operationId = protocolGateway.session(OperationMetaProtocol.class).operationId();

        if (element.hasStructureOf(referenceTo(UUID.class))) {
            value = operationId;
        } else if (element.hasStructureOf(referenceTo(String.class))) {
            value = operationId.toString();
        } else {
            throw new IllegalStateException("@OperationId element must have valueType of java.util.UUID or java.lang.String.");
        }

        return value;
    }

}
