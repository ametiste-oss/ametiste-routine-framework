package org.ametiste.routine.dsl.configuration.task.params;

import org.ametiste.dynamics.foundation.elements.AnnotatedRefProcessor;
import org.ametiste.dynamics.foundation.elements.AnnotatedRef;
import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.dsl.annotations.OperationName;
import org.ametiste.routine.dsl.annotations.ParamValueProvider;
import org.ametiste.routine.dsl.domain.OperationNameAnnotation;
import org.ametiste.routine.sdk.protocol.operation.OperationMetaProtocol;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * Provides task name to an element of a task marked by {@link OperationName}.
 * <p> Identifier can be resolved only as the {@link String} valueType. Other paramter types are unsupported and
 * will cause {@link IllegalStateException} at runtime.
 *
 * @implNote This provider using {@link OperationMetaProtocol} to resolve operation meta information.
 * @see OperationName
 * @see OperationNameAnnotation
 * @see OperationMetaProtocol
 * @since 1.1
 */
@Component
@ParamValueProvider
class OperationNameProvider extends AnnotatedRefProcessor<OperationNameAnnotation, String, ProtocolGateway> {

    public OperationNameProvider() {
        super(OperationNameAnnotation::new);
    }

    @Override
    protected void resolveValue(@NotNull final AnnotatedRef<String> element,
                                @NotNull final ProtocolGateway protocolGateway) {
        if (element.isRefeneceTo(String.class)) {
            element.feature(ref ->
                    ref.referencesTo(protocolGateway.session(OperationMetaProtocol.class).operationName())
            );
        } else {
            throw new IllegalStateException("@OperationName element must have valueType of java.lang.String.");
        }

    }

}
