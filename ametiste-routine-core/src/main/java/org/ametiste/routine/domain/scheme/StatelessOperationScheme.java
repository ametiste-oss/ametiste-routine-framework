package org.ametiste.routine.domain.scheme;

import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.ametiste.routine.sdk.operation.OperationExecutorFactory;
import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.function.Supplier;

/**
 * <p>
 * Stateless operation scheme definition. Will optimize {@link OperationExecutor} instances usage
 * by the single instance of the executor.
 * </p>
 *
 * <p>
 *     <b>WARNING:</b> since this scheme provides a single instance of the given {@code OperationExecutor}
 *     for the each operation execution, only stateless {@code OperationExecutor}s should be used with the scheme!
 * </p>
 *
 * @since 1.1
 */
public abstract class StatelessOperationScheme<T extends ParamsProtocol> extends AbstractOperationScheme<T> {

    private static class StatelessOperationExecutorFactory implements OperationExecutorFactory {

        private final OperationExecutor operationExecutor;

        public StatelessOperationExecutorFactory(Supplier<OperationExecutor> operationExecutor) {
            this.operationExecutor = operationExecutor.get();
        }

        @Override
        public OperationExecutor createExecutor() {
            return operationExecutor;
        }
    }

    public StatelessOperationScheme(final String name,
                                    final Supplier<T> paramsProtocolFactory,
                                    final Supplier<OperationExecutor> operationExecutor) {
        super(name, paramsProtocolFactory, new StatelessOperationExecutorFactory(operationExecutor));
    }

}
