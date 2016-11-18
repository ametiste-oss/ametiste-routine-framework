package org.ametiste.routine.dsl.configuration.task.params;

import org.ametiste.dynamics.foundation.elements.AnnotatedRefProcessor;
import org.ametiste.dynamics.foundation.elements.AnnotatedRef;
import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.dsl.annotations.OperationParameter;
import org.ametiste.routine.dsl.annotations.ParamValueProvider;
import org.ametiste.routine.dsl.application.DynamicParamsProtocol;
import org.ametiste.routine.dsl.domain.OperationParameterAnnotation;
import org.ametiste.routine.sdk.protocol.operation.OperationMetaProtocol;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

/**
 * Provides operation parameters using information provided by {@link OperationParameter} annotation.
 *
 * @implNote This provider using {@link OperationMetaProtocol} to resolve operation meta information.
 * <p>
 * Parameter types are resolved using {@link ConversionService}, so general extension mechanisms
 * provided by <i>Spring Framework Conversion System</i> may be adopted to extend paramter types resolving.
 * @see ConversionService
 * @see OperationParameterAnnotation
 * @see OperationParameter
 * @since 1.1
 */
@Component
@ParamValueProvider
class OperationParameterProvider extends AnnotatedRefProcessor<OperationParameterAnnotation, Object, ProtocolGateway> {

    private final ConversionService conversionService;

    @Autowired
    public OperationParameterProvider(ConversionService conversionService) {
        super(OperationParameterAnnotation::new);
        this.conversionService = conversionService;
    }

    @Override
    protected void resolveValue(@NotNull final AnnotatedRef<Object> element,
                                @NotNull final ProtocolGateway protocolGateway) {

        final String paramName = element.annotation(annotationSpec)
                // TODO: add method name, AnnotatedRef must provide #name() method
                .nameOrThrow(() -> new IllegalStateException("Can't resolve paramter name."));

        final Object value = protocolGateway
                .session(DynamicParamsProtocol.class).param(paramName);

        element.provideValue(conversionService.convert(value, element.type()));

    }

}

