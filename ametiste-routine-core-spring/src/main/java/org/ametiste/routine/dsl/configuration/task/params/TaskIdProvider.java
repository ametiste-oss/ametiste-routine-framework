package org.ametiste.routine.dsl.configuration.task.params;

import org.ametiste.dynamics.foundation.elements.AnnotatedRefProcessor;
import org.ametiste.dynamics.foundation.elements.AnnotatedRef;
import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.dsl.annotations.ParamValueProvider;
import org.ametiste.routine.dsl.annotations.TaskId;
import org.ametiste.routine.dsl.domain.TaskIdAnnotation;
import org.ametiste.routine.sdk.protocol.operation.OperationMetaProtocol;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Provides task identifier for task element marked as {@link TaskId}.
 * <p>
 * Identifier can be resolved as {@link String} or {@link UUID} valueType according to the
 * valueType of the annotated parameter. Other paramter types are unsupported and
 * will cause {@link IllegalStateException} at runtime.
 *
 * @implNote This provider using {@link OperationMetaProtocol} to resolve operation meta information.
 * @see TaskId
 * @see TaskIdAnnotation
 * @see OperationMetaProtocol
 * @since 1.1
 */
@Component
@ParamValueProvider
class TaskIdProvider extends AnnotatedRefProcessor<TaskIdAnnotation, Object, ProtocolGateway> {

    public TaskIdProvider() {
        super(TaskIdAnnotation::new);
    }

    @Override
    protected void resolveValue(@NotNull final AnnotatedRef<Object> element, @NotNull final ProtocolGateway gateway) {

        final Object value;
        final UUID taskId = gateway.session(OperationMetaProtocol.class).taskId();

        if (element.isRefeneceTo(UUID.class)) {
            value = taskId;
        } else if (element.isRefeneceTo(String.class)) {
            value = taskId.toString();
        } else {
            throw new IllegalStateException("@TaskId element must have valueType of java.util.UUID or java.lang.String.");
        }

        element.provideValue(value);

    }

}
