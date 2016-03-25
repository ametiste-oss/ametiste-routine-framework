package org.ametiste.routine.domain.scheme;

import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 * @since
 */
abstract public class AbstractOperationScheme<T extends ParamsProtocol> implements OperationScheme<T> {

    private final String name;
    private final Supplier<T> paramsProtocolFactory;

    public AbstractOperationScheme(String name, Supplier<T> paramsProtocolFactory) {
        this.name = name;
        this.paramsProtocolFactory = paramsProtocolFactory;
    }

    @Override
    final public String schemeName() {
        return name;
    }

    @Override
    final public void createOperationFor(final TaskOperationInstaller operationReceiver,
                                         final Consumer<T> paramsInstaller) {
        final T params = paramsProtocolFactory.get();
        paramsInstaller.accept(params);
        operationReceiver.addOperation(schemeName(), params);
    }

}
