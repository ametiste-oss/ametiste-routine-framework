package org.ametiste.routine.dsl.application;

import org.ametiste.dynamics.*;
import org.ametiste.dynamics.foundation.DynamicParameterElement;
import org.ametiste.lang.Triple;
import org.ametiste.laplatform.protocol.ProtocolGateway;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class MethodParamsValueResolver implements DynamicValueResolver<Method, Object, ProtocolGateway> {

    private final List<DynamicValueProvider<ProtocolGateway>> providers;

    MethodParamsValueResolver(final List<DynamicValueProvider<ProtocolGateway>> providers) {
        this.providers = providers;
    }

    public static final DynamicInitializer<Object, ProtocolGateway> resolve(final Method element,
                                                                            final List<DynamicValueProvider<ProtocolGateway>> providers) {
        return new MethodParamsValueResolver(providers).resolve(element);
    }

    @Override
    public DynamicInitializer<Object, ProtocolGateway> resolve(final Method method) {
        return (instance, protocolGateway) -> resolveParams(instance, method, protocolGateway)
                .mapSecondThird(InitializationData::of);
    }

    private Triple<Object, Method, Object[]> resolveParams(final Object instance,
                                                           final Method method,
                                                           final ProtocolGateway protocolGateway) {
        return Triple.of(instance, method,
            Stream.of(method.getParameters())
                .map(p -> resolveParamValue(p, instance, method, protocolGateway))
                .collect(Collectors.toList())
                .toArray(new Object[method.getParameterCount()])
        );
    }

    private Object resolveParamValue(final Parameter parameter,
                                     final Object instance,
                                     final Method method,
                                     final ProtocolGateway protocolGateway) {

        final DynamicParameterElement element = new DynamicParameterElement(parameter);

        // TODO : In case of fruther optimization this map calculation,
        // may be converted to some kind of function, these functions may be calculated at
        // #resolve method, before DynamicInitializer is created, and passed to application object.
        // So that only applicable providers will be invoked at runtime.
        // But this optimization will require some check method of provider which would work at start time
        return providers.stream().map(p -> p.provideValue(element, protocolGateway))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("There is no resolved value for the parameter '" +
                        parameter.getName() + "' of the method " + instance.getClass().getName() + "#" + method.getName()));
    }

}