package org.ametiste.routine.dsl.application;

import org.ametiste.dynamics.DynamicInvocation;
import org.ametiste.dynamics.DynamicValueProvider;
import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.metrics.MetricsService;
import org.ametiste.routine.domain.scheme.OperationScheme;
import org.ametiste.routine.domain.scheme.TaskOperationInstaller;
import org.ametiste.routine.sdk.operation.OperationExecutor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DynamicOperationScheme implements OperationScheme<DynamicParamsProtocol> {

    private final String operationName;

    private final DynamicInvocation<Object, ProtocolGateway> invocation;

    public DynamicOperationScheme(final String operationName,
                                  final Class<?> controllerClass,
                                  final Method method,
                                  final List<DynamicValueProvider<ProtocolGateway>> fieldValueProviders,
                                  final List<DynamicValueProvider<ProtocolGateway>> paramValueProviders) {
        this.operationName = operationName;
        // TODO: may be construct invocation in the context? Here I just can have some data to show and invocation
        this.invocation = createInvocation(controllerClass, method, fieldValueProviders, paramValueProviders);
    }

    @Override
    public String schemeName() {
        return operationName;
    }

    @Override
    public OperationExecutor operationExecutor() {
        return (feedback, gateway) -> invocation.invoke(gateway);
    }

    @Override
    public void createOperationFor(final TaskOperationInstaller operationReceiver,
                                   final Consumer<DynamicParamsProtocol> paramsInstaller) {
        operationReceiver.addOperation(operationName, null);
    }

    private static final Supplier<Object> controllerClassConstructor(Class<?> controllerClass) {
        return () -> {
            try {
                return controllerClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static final DynamicInvocation<Object, ProtocolGateway> createInvocation(final Class<?> controllerClass,
                                                                                    final Method method,
                                                                                    final List<DynamicValueProvider<ProtocolGateway>> fieldValueProviders,
                                                                                    final List<DynamicValueProvider<ProtocolGateway>> paramValueProviders) {
        return new DynamicInvocation<>(
                controllerClassConstructor(controllerClass),
                method,
                FieldValueResolver.resolve(controllerClass, fieldValueProviders),
                MethodParamsValueResolver.resolve(method, paramValueProviders)
        );
    }


}