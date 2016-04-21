package org.ametiste.routine.dsl.configuration.task.params;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.dsl.annotations.OperationName;
import org.ametiste.routine.dsl.annotations.ParamValueProvider;
import org.ametiste.dynamics.foundation.AnnotatedElementValueProvider;
import org.ametiste.dynamics.SurfaceElement;
import org.ametiste.routine.sdk.protocol.operation.OperationMetaProtocol;
import org.springframework.stereotype.Component;

import static org.ametiste.dynamics.foundation.structure.ReflectFoundation.referenceTo;

/**
 * <p>
 *     Resolves task id for parameter marked as {@link OperationName}.
 * </p>
 *
 * <p>
 *     This provider using {@link OperationMetaProtocol} to resolve operation meta information.
 * </p>
 *
 * <p>
 *     Identifier can be resolved only as {@link String} valueType. Other paramter types are unsupported and
 *     will cause {@link IllegalStateException} at runtime.
 * </p>
 *
 * @see OperationName
 * @see OperationMetaProtocol
 * @since 1.1
 */
@Component
@ParamValueProvider
class OperationNameProvider extends AnnotatedElementValueProvider<ProtocolGateway> {

    public OperationNameProvider() {
        super(OperationName.class);
    }

    @Override
    protected Object resolveValue(final SurfaceElement element,
                                  final ProtocolGateway protocolGateway) {

        if (element.hasStructureOf(referenceTo(String.class))) {
            return protocolGateway.session(OperationMetaProtocol.class).operationName();
        } else {
            throw new IllegalStateException("@TaskName element must have valueType of java.lang.String.");
        }

    }

}
