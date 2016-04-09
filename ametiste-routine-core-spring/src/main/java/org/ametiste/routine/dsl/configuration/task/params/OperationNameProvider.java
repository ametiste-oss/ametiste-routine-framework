package org.ametiste.routine.dsl.configuration.task.params;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.dsl.annotations.OperationName;
import org.ametiste.routine.dsl.annotations.TaskName;
import org.ametiste.routine.dsl.application.AnnotatedParameterProvider;
import org.ametiste.routine.meta.util.MetaMethodParameter;
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
 *     Identifier can be resolved only as {@link String} type. Other paramter types are unsupported and
 *     will cause {@link IllegalStateException} at runtime.
 * </p>
 *
 * @see OperationName
 * @see OperationMetaProtocol
 * @since 1.1
 */
@Component
class OperationNameProvider extends AnnotatedParameterProvider {

    public OperationNameProvider() {
        super(OperationName.class);
    }

    @Override
    protected Object resolveParameterValue(final MetaMethodParameter parameter,
                                           final ProtocolGateway protocolGateway) {

        if (String.class.isAssignableFrom(parameter.type())) {
            return protocolGateway.session(OperationMetaProtocol.class).operationName();
        } else {
            throw new IllegalStateException("@TaskName parameter must have type of java.lang.String.");
        }

    }

}
