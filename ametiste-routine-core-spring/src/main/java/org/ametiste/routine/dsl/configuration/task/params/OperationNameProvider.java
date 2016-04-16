package org.ametiste.routine.dsl.configuration.task.params;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.dsl.annotations.OperationName;
import org.ametiste.routine.dsl.annotations.ParamValueProvider;
import org.ametiste.routine.dsl.application.AnnotatedElementValueProvider;
import org.ametiste.routine.dsl.application.RuntimeElement;
import org.ametiste.routine.sdk.protocol.operation.OperationMetaProtocol;
import org.springframework.stereotype.Component;

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
class OperationNameProvider extends AnnotatedElementValueProvider {

    public OperationNameProvider() {
        super(OperationName.class);
    }

    @Override
    protected Object resolveValue(final RuntimeElement element,
                                  final ProtocolGateway protocolGateway) {

        if (element.mayHaveValueOf(String.class)) {
            return protocolGateway.session(OperationMetaProtocol.class).operationName();
        } else {
            throw new IllegalStateException("@TaskName element must have valueType of java.lang.String.");
        }

    }

}
