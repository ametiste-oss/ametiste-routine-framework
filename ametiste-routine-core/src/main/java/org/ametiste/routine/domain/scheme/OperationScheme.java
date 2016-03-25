package org.ametiste.routine.domain.scheme;

import org.ametiste.routine.domain.task.Operation;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.function.Consumer;

/**
 *
 * @since
 */
public interface OperationScheme<T extends ParamsProtocol> {

    String schemeName();

    void createOperationFor(TaskOperationReceiver operationReceiver, Consumer<T> paramsInstaller);

}

