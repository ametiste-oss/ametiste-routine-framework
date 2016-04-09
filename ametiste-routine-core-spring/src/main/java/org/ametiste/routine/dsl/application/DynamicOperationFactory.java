package org.ametiste.routine.dsl.application;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.laplatform.sdk.protocol.Protocol;
import org.ametiste.routine.dsl.annotations.Connect;
import org.ametiste.routine.meta.util.*;
import org.ametiste.routine.sdk.operation.OperationFeedback;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DynamicOperationFactory {

    private final Method method;
    private final Class<?> controllerClass;
    private final List<ParamaterProvider> paramaterProviders;

    public DynamicOperationFactory(
            final Class<?> controllerClass,
            final Method method,
            final List<ParamaterProvider> paramaterProviders) {
        this.controllerClass = controllerClass;
        this.method = method;
        this.paramaterProviders = paramaterProviders;
    }

    public DynamicOperation createDynamicOperation(final OperationFeedback operationFeedback,
                                                   final ProtocolGateway protocolGateway) {

        // Вот это можно сделать один раз и сюда передавать уже данные о том, какие инжекты нужны будут

        final Object controllerInstance = createOperationControllerInstance();
        final MetaMethod metaMethod = MetaMethod.of(MetaObject.of(controllerInstance), method);

        Stream.of(controllerClass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Connect.class))
                .forEach(f -> {
                    // TODO: add exception, if @Connect does not point on LambdaProtocol field
                    final Protocol session = protocolGateway
                            .session((Class<? extends Protocol>) f.getType());
                    ReflectionUtils.makeAccessible(f);
                    ReflectionUtils.setField(f, controllerInstance, session);
                });

        final Object[] params = metaMethod.streamOfParameters()
                .map(p -> resolveMethodParameters(p, protocolGateway))
                .collect(Collectors.toList())
                .toArray(new Object[metaMethod.paramsCount()]);

        return () -> {
            try {
                method.invoke(controllerInstance, params);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private Object resolveMethodParameters(MetaMethodParameter methodParameter, final ProtocolGateway protocolGateway) {
        return paramaterProviders.stream()
            .map(p -> p.provideValue(methodParameter, protocolGateway))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("There is no resolved parameters."));
    }

    private Object createOperationControllerInstance() {
        final Object controllerInstance;
        try {
            controllerInstance = controllerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return controllerInstance;
    }

}