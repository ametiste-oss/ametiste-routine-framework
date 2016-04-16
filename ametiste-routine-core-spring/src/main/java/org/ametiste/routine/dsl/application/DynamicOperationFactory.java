package org.ametiste.routine.dsl.application;

import org.ametiste.lang.Let;
import org.ametiste.lang.Pair;
import org.ametiste.lang.Triple;
import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.sdk.operation.OperationFeedback;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class GeneralElementDelegate {

    private final String name;
    private final Class<?> supportedValueType;

    public GeneralElementDelegate(String name, Class<?> supportedValueType) {
        this.name = name;
        this.supportedValueType = supportedValueType;
    }

    public <T> T mapName(final Function<String, T> transform) {
        return transform.apply(name);
    }

    public boolean mayHaveValueOf(final Class<?> valueType) {
        return valueType.isAssignableFrom(supportedValueType);
    }

    public Class<?> valueType() {
        return supportedValueType;
    }

}

abstract class AnnotatedObjectElement<T extends AnnotatedElement>
        implements RuntimeElement {

    protected final T element;
    private final GeneralElementDelegate delegate;

    public AnnotatedObjectElement(T element, GeneralElementDelegate delegate) {
        this.element = element;
        this.delegate = delegate;
    }

    @Override
    public boolean isAnnotatedBy(final Class<? extends Annotation> annotation) {
        return element.isAnnotationPresent(annotation);
    }

    @Override
    public <S extends Annotation, T> T annotationValue(final Class<S> annotation, final Function<S, T> valueProvider) {
        return valueProvider.apply(element.getDeclaredAnnotation(annotation));
    }

    @Override
    public <T> T mapName(final Function<String, T> transform) {
        return delegate.mapName(transform);
    }

    @Override
    public boolean mayHaveValueOf(final Class<?> valueType) {
        return delegate.mayHaveValueOf(valueType);
    }

    @Override
    public Class<?> valueType() {
        return delegate.valueType();
    }

}

class OperationParameterElement extends AnnotatedObjectElement<Parameter> {

    public OperationParameterElement(Parameter parameter) {
        super(parameter, new GeneralElementDelegate(parameter.getName(), parameter.getType()));
    }

}

class OperationFieldElement extends AnnotatedObjectElement<Field> {

    public OperationFieldElement(Field field) {
        super(field, new GeneralElementDelegate(field.getName(), field.getType()));
    }

}


interface ValueApplication<T> {

    void apply(T instance);

}

interface RuntimeElementValueResolver<T, S> {

    ValueApplication<S> resolve(T element, ProtocolGateway protocolGateway);

}

class FieldValueResolver implements RuntimeElementValueResolver<Class<?>, Object> {

    private final List<RuntimeElementValueProvider> providers;

    FieldValueResolver(final List<RuntimeElementValueProvider> providers) {
        this.providers = providers;
    }

    @Override
    public ValueApplication<Object> resolve(final Class<?> element,
                                            final ProtocolGateway protocolGateway) {
        return (instance) -> {
            Stream.of(element.getDeclaredFields())
                    .map(f -> fieldValue(f, instance, protocolGateway))
                    .forEach(v -> applyValue(instance, v));
        };
    }

    private Pair<Field, Object> fieldValue(final Field field,
                                           final Object instance,
                                           final ProtocolGateway protocolGateway) {

        final OperationFieldElement element = new OperationFieldElement(field);

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

class MethodParamsValueResolver implements RuntimeElementValueResolver<Method, Object> {

    private final List<RuntimeElementValueProvider> providers;

    MethodParamsValueResolver(final List<RuntimeElementValueProvider> providers) {
        this.providers = providers;
    }

    @Override
    public ValueApplication<Object> resolve(final Method method, final ProtocolGateway protocolGateway) {
        return (instance) -> resolveParams(instance, method, protocolGateway).let(this::invokeMethod);
    }

    private Let<Triple<Object, Method, Object[]>> resolveParams(final Object instance,
                                                                final Method method,
                                                                final ProtocolGateway protocolGateway) {
        return Triple.let(instance, method,
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
        final OperationParameterElement element = new OperationParameterElement(parameter);
        return providers.stream().map(p -> p.provideValue(element, protocolGateway))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("There is no resolved value for the parameter '" +
                        parameter.getName() + "' of the method " + instance.getClass().getName() + "#" + method.getName()));
    }

    private void invokeMethod(final Triple<Object, Method, Object[]> params) {
        try {
            params.second.invoke(params.first, params.third);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}

public class DynamicOperationFactory {

    private final Method method;
    private final Class<?> controllerClass;
    private final List<RuntimeElementValueProvider> paramValueProviders;
    private final List<RuntimeElementValueProvider> fieldValueProviders;

    public DynamicOperationFactory(
            final Class<?> controllerClass,
            final Method method,
            final List<RuntimeElementValueProvider> paramValueProviders,
            final List<RuntimeElementValueProvider> fieldValueProviders) {
        this.controllerClass = controllerClass;
        this.method = method;
        this.paramValueProviders = paramValueProviders;
        this.fieldValueProviders = fieldValueProviders;
    }

    public DynamicOperation createDynamicOperation(final OperationFeedback operationFeedback,
                                                   final ProtocolGateway protocolGateway) {

        // TODO : In case of optimization these calculations of target fields and parameters may be
        // extracted to the constructor, see dsl-cleanup branch for draft implementation

        final Object instance = createOperationControllerInstance();

        Stream.of(controllerClass.getDeclaredFields())
                .forEach(f -> resolveFieldValue(f, instance, protocolGateway));

        final Object[] params = Stream.of(method.getParameters())
                .map(p -> resolveParamValue(p, protocolGateway))
                .collect(Collectors.toList())
                .toArray(new Object[method.getParameterCount()]);

        return () -> {
            try {
                method.invoke(instance, params);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private void resolveFieldValue(final Field field,
                                   final Object instance,
                                   final ProtocolGateway protocolGateway) {

        final Object value = fieldValueProviders.stream()
            .map(p -> p.provideValue(new OperationFieldElement(field), protocolGateway))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("There is no resolved value for the element '" +
                    field.getName() + "' of the " + controllerClass.getName()));

        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, instance, value);

    }

    private Object resolveParamValue(final Parameter parameter, final ProtocolGateway protocolGateway) {
        return paramValueProviders.stream()
            .map(p -> p.provideValue(new OperationParameterElement(parameter), protocolGateway))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("There is no resolved value for the parameter '" +
                        parameter.getName() + "' of the method " + controllerClass.getName() + "#" + method.getName()));
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