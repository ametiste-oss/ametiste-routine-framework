package org.ametiste.routine.domain.scheme;

import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.Map;
import java.util.function.Consumer;

/**
 *
 * @since
 */
public interface TaskOperationReceiver {

    <S extends ParamsProtocol> void addOperation(final Class<? extends OperationScheme<S>> operationScheme,
                                                                  final Consumer<S> paramsInstaller);
}
