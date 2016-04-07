package org.ametiste.routine.dsl.application;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.laplatform.sdk.protocol.Protocol;
import org.ametiste.routine.dsl.annotations.Connect;
import org.ametiste.routine.dsl.annotations.OperationParameter;
import org.ametiste.routine.sdk.operation.OperationFeedback;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

public class DynamicOperationFactory {

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