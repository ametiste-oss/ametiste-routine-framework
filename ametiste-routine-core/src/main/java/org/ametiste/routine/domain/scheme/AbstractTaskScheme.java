package org.ametiste.routine.domain.scheme;

import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 * @since
 */
public abstract class AbstractTaskScheme<T extends ParamsProtocol> implements TaskScheme<T> {

    private final String schemeName;
    private final Supplier<T> paramsProtocolFactory;

    public AbstractTaskScheme(String schemeName, Supplier<T> paramsProtocolFactory) {
        this.schemeName = schemeName;
        this.paramsProtocolFactory = paramsProtocolFactory;
    }

    @Override
    final public void setupTask(final TaskBuilder<T> taskBuilder,
                                final Consumer<T> paramsInstaller,
                                final String creatorIdenifier) throws TaskCreationRejectedBySchemeException {

        final T paramsProtocol = paramsProtocolFactory.get();
        paramsInstaller.accept(paramsProtocol);

        verifyCreationRequest(paramsProtocol, creatorIdenifier);
        fulfillOperations(taskBuilder::addOperation, paramsProtocol);
        fulfillProperties(taskBuilder::addProperty, paramsProtocol);
    }

    protected void verifyCreationRequest(T schemeParams, String creatorIdentifier) throws TaskCreationRejectedBySchemeException {}

    protected void fulfillProperties(TaskPropertiesReceiver propertiesReceiver, T schemeParams) {}

    protected void fulfillOperations(TaskOperationReceiver operationReceiver, T schemeParams) {

    }

    @Override
    public String schemeName() {
        return schemeName;
    }

}
