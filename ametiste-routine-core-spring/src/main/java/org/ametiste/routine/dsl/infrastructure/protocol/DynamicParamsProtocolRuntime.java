package org.ametiste.routine.dsl.infrastructure.protocol;

import org.ametiste.laplatform.sdk.protocol.ProtocolFactory;
import org.ametiste.routine.domain.scheme.OperationScheme;
import org.ametiste.routine.domain.task.ExecutionLine;
import org.ametiste.routine.dsl.application.DynamicParamsProtocol;
import org.ametiste.routine.infrastructure.execution.OperationRuntime;
import org.ametiste.routine.sdk.operation.OperationFeedback;

import java.util.UUID;

/**
 * <p>
 *     Oepration Runtime Protoyocol for {@link DynamicParamsProtocol}. This runtime is
 *     factory that used during operation runtime to create protocol factory
 *     that bound to the current executing operation runtime properties.
 * </p>
 *
 * @see OperationRuntime
 * @see DynamicParamsProtocol
 * @since 1.1
 */
public class DynamicParamsProtocolRuntime implements OperationRuntime<ProtocolFactory<DynamicParamsProtocol>> {

    @Override
    // TODO: how can I register it at mod, so that I can see this factory/protocol via http interface
    // TODO: how can I describe its @ProtocolMeta paramteres? So that I can have auto-metrics for such protocols
    public ProtocolFactory<DynamicParamsProtocol> createRuntimeBoundObject(final UUID taskId,
                                                                           final ExecutionLine executionLine,
                                                                           final OperationScheme operationScheme,
                                                                           final OperationFeedback feedbackController) {
        return gw -> new DirectDynamicParamsProtocol(executionLine.properties());
    }

}
