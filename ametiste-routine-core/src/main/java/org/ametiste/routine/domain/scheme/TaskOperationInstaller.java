package org.ametiste.routine.domain.scheme;

import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.function.Consumer;

import static javafx.scene.input.KeyCode.T;

/**
 *
 * @since
 */
public interface TaskOperationInstaller {

    <S extends ParamsProtocol> void addOperation(final Class<? extends OperationScheme<S>> operationScheme,
                                                                  final Consumer<S> paramsInstaller);

}
