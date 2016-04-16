package org.ametiste.routine.dsl.configuration.task.params;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.dsl.annotations.ParamValueProvider;
import org.ametiste.routine.dsl.annotations.TaskId;
import org.ametiste.routine.dsl.application.AnnotatedElementValueProvider;
import org.ametiste.routine.dsl.application.RuntimeElement;
import org.ametiste.routine.sdk.protocol.operation.OperationMetaProtocol;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * <p>
 *     Resolves task id for parameter marked as {@link TaskId}.
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
 * @see TaskId
 * @see OperationMetaProtocol
 * @since 1.1
 */
@Component
@ParamValueProvider
class TaskIdProvider extends AnnotatedElementValueProvider {

    public TaskIdProvider() {
        super(TaskId.class);
    }

    @Override
    protected Object resolveValue(final RuntimeElement element,
                                  final ProtocolGateway protocolGateway) {

        final Object value;
        final UUID taskId = protocolGateway.session(OperationMetaProtocol.class).taskId();

        if (element.mayHaveValueOf(UUID.class)) {
            value = taskId;
        } else if (element.mayHaveValueOf(String.class)) {
            value = taskId.toString();
        } else {
            throw new IllegalStateException("@TaskId element must have valueType of java.util.UUID or java.lang.String.");
        }

        return value;
    }

}
