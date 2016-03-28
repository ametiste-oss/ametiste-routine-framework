package org.ametiste.routine.configuration;

import org.ametiste.laplatform.protocol.Protocol;
import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.app.annotations.Connect;
import org.ametiste.routine.app.annotations.RoutineTask;
import org.ametiste.routine.app.annotations.SchemeMapping;
import org.ametiste.routine.app.annotations.TaskOperation;
import org.ametiste.routine.domain.scheme.*;
import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.ametiste.routine.sdk.operation.OperationFeedback;
import org.ametiste.routine.sdk.protocol.operation.AbstractParamProtocol;
import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

interface DynamicOperation {

    void run();

}

class DynamicOperationFactory {

    private final Method method;
    private final Class<?> controllerClass;

    public DynamicOperationFactory(Class<?> controllerClass, Method method) {
        this.controllerClass = controllerClass;

//        final List<Annotation[][]> collect = operations.stream().map(
//                op -> op.getParameterAnnotations()
//        ).collect(Collectors.toList());

        this.method = method;
    }

    public DynamicOperation createDynamicOperation(final OperationFeedback operationFeedback, final ProtocolGateway protocolGateway) {

        final Object controllerInstance;

        try {
            controllerInstance = controllerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // Вот это можно сделать один раз и сюда передавать уже данные о том, какие инжекты нужны будут

        Stream.of(controllerClass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Connect.class))
                .forEach(f -> {
                    // TODO: add exception, if @Connect does not point on Protocol field
                    final Protocol session = protocolGateway.session((Class<? extends Protocol>) f.getType());
                    ReflectionUtils.setField(f, controllerInstance, session);
                });

        return () -> {
            try {
                method.invoke(controllerInstance, new Object[] {});
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }

}

class DynamicOperationExecutor implements OperationExecutor {

    private final DynamicOperationFactory operationFactory;

    public DynamicOperationExecutor(final DynamicOperationFactory operationFactory) {
        this.operationFactory = operationFactory;
    }

    @Override
    public void execOperation(final OperationFeedback feedback, final ProtocolGateway protocolGateway) {
        operationFactory.createDynamicOperation(feedback, protocolGateway).run();
    }

}

class DynamicParamsProtocol extends AbstractParamProtocol {

    protected DynamicParamsProtocol(final List<String> definedParams, final Map<String, String> params) {
        super(definedParams, params);
    }

}

class DynamicOperationScheme implements OperationScheme<DynamicParamsProtocol> {

    private final String operationName;
    private final DynamicOperationFactory opFactory;

    public DynamicOperationScheme(final String operationName, final DynamicOperationFactory opFactory) {
        this.operationName = operationName;
        this.opFactory = opFactory;
    }

    @Override
    public String schemeName() {
        return operationName;
    }

    @Override
    public OperationExecutor operationExecutor() {
        return new DynamicOperationExecutor(opFactory);
    }

    @Override
    public void createOperationFor(final TaskOperationInstaller operationReceiver,
                                   final Consumer<DynamicParamsProtocol> paramsInstaller) {
        operationReceiver.addOperation(operationName, null);
    }

}

class Pair<F, S> {

    final F first;
    final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public static final <F, S> Pair<F, S> of(F first, S second) {
        return new Pair<>(first, second);
    }

}

/**
 *
 * @since
 */
@Configuration
public class AppConfiguration {

    @Autowired
    @RoutineTask
    private List<Object> taskControllers;

    @Autowired
    private SchemeRepository schemeRepository;

    @Bean
    public Object testConfig() {
        taskControllers.stream()
                .map(Object::getClass)
                .forEach(this::mapToTaskScheme);
        return new Object();
    }

    private TaskScheme mapToTaskScheme(Class<?> controllerClass) {

        final String schemeName = controllerClass
                .getDeclaredAnnotation(SchemeMapping.class)
                .schemeName();

        final List<DynamicOperationScheme> operations = Stream
                .of(ReflectionUtils.getAllDeclaredMethods(controllerClass))
                .filter(m -> m.isAnnotationPresent(TaskOperation.class))
                .map(m -> Pair.of(resolveOperationName(m), new DynamicOperationFactory(controllerClass, m)))
                .map(p -> new DynamicOperationScheme(p.first, p.second))
                .collect(Collectors.toList());

        operations.forEach(schemeRepository::saveScheme);

        return new TaskScheme<DynamicParamsProtocol>() {

            @Override
            public String schemeName() {
                return schemeName;
            }

            @Override
            public void setupTask(final TaskBuilder<DynamicParamsProtocol> taskBuilder, final Consumer<DynamicParamsProtocol> paramsInstaller, final String creatorIdenifier) throws TaskSchemeException {

                // TODO: atm there is no paramteres

                final DynamicParamsProtocol dynamicParamsProtocol
                        = new DynamicParamsProtocol(Collections.emptyList(), Collections.emptyMap());

                operations.forEach(
                    op -> taskBuilder.addOperation(op.schemeName(), dynamicParamsProtocol)
                );
            }
        };
    }

    private String resolveOperationName(final Method m) {

        final String resolvedOpName;
        final String declaredName = m.getDeclaredAnnotation(TaskOperation.class).schemeName();

        if(declaredName.isEmpty()) {
            resolvedOpName = m.getName();
        } else {
            resolvedOpName = declaredName;
        }

        return resolvedOpName;
    }

}
