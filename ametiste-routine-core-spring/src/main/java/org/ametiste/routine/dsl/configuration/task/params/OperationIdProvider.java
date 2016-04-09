package org.ametiste.routine.dsl.configuration.task.params;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.dsl.annotations.OperationId;
import org.ametiste.routine.dsl.annotations.OperationParameter;
import org.ametiste.routine.dsl.application.AnnotatedParameterProvider;
import org.ametiste.routine.dsl.application.DynamicParamsProtocol;
import org.ametiste.routine.meta.util.MetaMethodParameter;
import org.ametiste.routine.sdk.protocol.operation.OperationMetaProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.UUID;

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
 *     Identifier can be resolved as {@link String} or {@link UUID} type according to the
 *     type of the annotated parameter. Other paramter types are unsupported and
 *     will cause {@link IllegalStateException} at runtime.
 * </p>
 *
 * @see OperationId
 * @see OperationMetaProtocol
 * @since 1.1
 */
@Component
class OperationIdProvider extends AnnotatedParameterProvider {

    public OperationIdProvider() {
        super(OperationId.class);
    }

    @Override
    protected Object resolveParameterValue(final MetaMethodParameter parameter,
                                           final ProtocolGateway protocolGateway) {

        final Object value;
        final UUID operationId = protocolGateway.session(OperationMetaProtocol.class).operationId();

        if (UUID.class.isAssignableFrom(parameter.type())) {
            value = operationId;
        } else if (String.class.isAssignableFrom(parameter.type())) {
            value = operationId.toString();
        } else {
            throw new IllegalStateException("@OperationId parameter must have type of java.util.UUID or java.lang.String.");
        }

        return value;
    }

}
