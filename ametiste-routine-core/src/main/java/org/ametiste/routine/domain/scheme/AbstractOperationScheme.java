package org.ametiste.routine.domain.scheme;

import org.ametiste.routine.sdk.operation.OperationExecutor;
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
    private final Supplier<OperationExecutor> operationExecutorFactory;

    public AbstractOperationScheme(String name, Supplier<T> paramsProtocolFactory, Supplier<OperationExecutor> operationExecutorFactory) {
        this.name = name;
        this.paramsProtocolFactory = paramsProtocolFactory;
        this.operationExecutorFactory = operationExecutorFactory;
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

    @Override
    public OperationExecutor operationExecutor() {
        return operationExecutorFactory.get();
    }

}
