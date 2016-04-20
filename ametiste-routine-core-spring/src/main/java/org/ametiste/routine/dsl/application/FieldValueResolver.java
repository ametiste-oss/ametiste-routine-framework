package org.ametiste.routine.dsl.application;

import org.ametiste.dynamics.*;
import org.ametiste.dynamics.foundation.DynamicFieldElement;
import org.ametiste.lang.Pair;
import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

class FieldValueResolver implements DynamicValueResolver<Class<?>, Object, ProtocolGateway> {

    private final List<DynamicValueProvider<ProtocolGateway>> providers;

    FieldValueResolver(final List<DynamicValueProvider<ProtocolGateway>> providers) {
        this.providers = providers;
    }

    public static final DynamicInitializer<Object, ProtocolGateway> resolve(final Class<?> element,
                                                                            final List<DynamicValueProvider<ProtocolGateway>> providers) {
        return new FieldValueResolver(providers).resolve(element);
    }

    @Override
    public DynamicInitializer<Object, ProtocolGateway> resolve(final Class<?> element) {
        return (instance, protocolGateway) -> {
            Stream.of(element.getDeclaredFields())
                    .map(f -> fieldValue(f, instance, protocolGateway))
                    .forEach(v -> applyValue(instance, v));
            return InitializationData.empty();
        };
    }

    private Pair<Field, Object> fieldValue(final Field field,
                                           final Object instance,
                                           final ProtocolGateway protocolGateway) {

        final DynamicFieldElement element = new DynamicFieldElement(field);

        // TODO : In case of fruther optimization this map calculation,
        // may be converted to some kind of function, these functions may be calculated at
        // #resolve method, before DynamicInitializer is created, and passed to application object.
        // So that only applicable providers will be invoked at runtime.
        // But this optimization will require some check method of provider which would work at start time
        return providers.stream().map(p -> p.provideValue(element, protocolGateway))
                .filter(Optional::isPresent)
                .map(v -> Pair.of(field, v.get()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("There is no resolved value for the element '" +
                        field.getName() + "' of the " + instance.getClass().getName()));
    }

    private void applyValue(final Object instance,
                            final Pair<Field, Object> fieldValue) {
        ReflectionUtils.makeAccessible(fieldValue.first);
        ReflectionUtils.setField(fieldValue.first, instance, fieldValue.second);
    }

}