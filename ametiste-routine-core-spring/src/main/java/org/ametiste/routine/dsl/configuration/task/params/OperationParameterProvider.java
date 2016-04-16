package org.ametiste.routine.dsl.configuration.task.params;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.dsl.annotations.OperationParameter;
import org.ametiste.routine.dsl.annotations.ParamValueProvider;
import org.ametiste.routine.dsl.application.AnnotatedElementValueProvider;
import org.ametiste.routine.dsl.application.DynamicParamsProtocol;
import org.ametiste.routine.dsl.application.RuntimeElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

/**
 * Resolves operation parameters using information provided by {@link OperationParameter} annotation.
 * <p>
 * This provider using {@link DynamicParamsProtocol} to resolve operation paramters at operation runtime.
 * <p>
 * Parameter types are resolved using {@link ConversionService}, so general extension mechanisms
 * provided by <i>Spring Framework Conversion System</i> may be adopted to extend paramter types resolving.
 *
 * @see ConversionService
 * @see OperationParameter
 *
 * @since 1.1
 */
@Component
@ParamValueProvider
class OperationParameterProvider extends AnnotatedElementValueProvider {

    private final ConversionService conversionService;

    @Autowired
    public OperationParameterProvider(ConversionService conversionService) {
        super(OperationParameter.class);
        this.conversionService = conversionService;
    }

    @Override
    protected Object resolveValue(final RuntimeElement element,
                                  final ProtocolGateway protocolGateway) {

        // NOTE: this internal method invoked only if the given annotation is exists on the element
        Object value = protocolGateway.session(DynamicParamsProtocol.class)
                .param(element.annotationValue(OperationParameter.class, OperationParameter::value));

        return conversionService.convert(value, element.valueType());
    }

}

