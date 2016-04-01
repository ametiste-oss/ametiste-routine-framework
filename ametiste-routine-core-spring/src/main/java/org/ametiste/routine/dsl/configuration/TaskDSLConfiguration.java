package org.ametiste.routine.dsl.configuration;

import org.ametiste.laplatform.protocol.GatewayContext;
import org.ametiste.laplatform.protocol.Protocol;
import org.ametiste.laplatform.protocol.ProtocolFactory;
import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.domain.scheme.*;
import org.ametiste.routine.dsl.annotations.*;
import org.ametiste.routine.interfaces.taskdsl.service.DynamicParamsProtocol;
import org.ametiste.routine.interfaces.taskdsl.service.DynamicTaskService;
import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.ametiste.routine.sdk.operation.OperationFeedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
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
//        final Annotation[][] parameterAnnotations = ;
        this.method = method;
    }

    public DynamicOperation createDynamicOperation(final OperationFeedback operationFeedback, final ProtocolGateway protocolGateway) {

        final Object controllerInstance;
        final Object[] params = new Object[method.getParameterAnnotations().length];
        final Class<?>[] parameterTypes = method.getParameterTypes();

        try {
            controllerInstance = controllerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // Вот это можно сделать один раз и сюда передавать уже данные о том, какие инжекты нужны будут

        Stream.of(controllerClass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Connect.class))
                .forEach(f -> {
                    // TODO: add exception, if @Connect does not point on LambdaProtocol field

                    final Protocol session = protocolGateway
                            .session((Class<? extends Protocol>) f.getType());

                    ReflectionUtils.makeAccessible(f);
                    ReflectionUtils.setField(f, controllerInstance, session);
                });

        int p = 0;

        for (Annotation[] parameterType : method.getParameterAnnotations()) {
            // NOTE: take only first parameter annotation in account, other annotations just ignored
            if (parameterType[0] instanceof OperationParameter) {
                params[p] = protocolGateway.session(DynamicParamsProtocol.class).param(
                        ((OperationParameter) parameterType[0]).value());
            } else {
                throw new IllegalStateException("");
            }
            p++;
        }

        return () -> {
            try {
                method.invoke(controllerInstance, params);
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
public class TaskDSLConfiguration {

    @Autowired(required = false)
    @RoutineTask
    private List<Object> taskControllers;

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    private TaskIssueService taskIssueService;

    @Bean
    @Scope(scopeName = "prototype")
    public DynamicParamsProtocol dynamicParamsProtocol(GatewayContext context) {
        return new DynamicParamsProtocol(context.lookupMap("params"));
    }

    @Bean
    public ProtocolFactory<DynamicParamsProtocol> dynamicParamsProtocolConnection() {
        return c -> dynamicParamsProtocol(c);
    }

    @Bean
    public DynamicTaskService dynamicTaskService() {
        return new DynamicTaskService(schemeRepository, taskIssueService);
    }

    @Bean
    public Object testDSLConfig() {
        if (taskControllers != null) {
            taskControllers.stream()
                    .map(Object::getClass)
                    .map(this::mapToTaskScheme)
                    .forEach(schemeRepository::saveScheme);

        }
        return new Object();
    }

    private TaskScheme mapToTaskScheme(Class<?> controllerClass) {

        if (!controllerClass.isAnnotationPresent(RoutineTask.class)) {
            throw new IllegalArgumentException("Only @RoutineTask classes are allowed.");
        }

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

                final DynamicParamsProtocol dynamicParamsProtocol = new DynamicParamsProtocol();

                paramsInstaller.accept(dynamicParamsProtocol);

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
