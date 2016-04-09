package org.ametiste.routine.dsl.configuration.task.params;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.laplatform.protocol.gateway.ProtocolGatewayService;
import org.ametiste.routine.dsl.annotations.OperationParameter;
import org.ametiste.routine.dsl.application.AnnotatedParameterProvider;
import org.ametiste.routine.dsl.application.DynamicParamsProtocol;
import org.ametiste.routine.meta.util.MetaMethodParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * <p>
 *     Resolves operation paramteres using information provided by {@link OperationParameter} annotation.
 * </p>
 *
 * <p>
 *     This provider using {@link DynamicParamsProtocol} to resolve operation paramters at operation runtime.
 * </p>
 *
 * <p>
 *     Paramter types are resolved using {@link ConversionService}, so general extension mechanisms
 *     provided by <i>Spring Framework Conversion System</i> may be adopted to extend paramter types resolving.
 * </p>
 *
 * @see ConversionService
 * @see OperationParameter
 *
 * @since 1.1
 */
@Component
class OperationParameterProvider extends AnnotatedParameterProvider {

    private final ConversionService conversionService;

    @Autowired
    public OperationParameterProvider(ConversionService conversionService) {
        super(OperationParameter.class);
        this.conversionService = conversionService;
    }

    @Override
    protected Object resolveParameterValue(final MetaMethodParameter parameter,
                                           final ProtocolGateway protocolGateway) {

        Object value = protocolGateway.session(DynamicParamsProtocol.class)
                .param(parameter.annotationValue(OperationParameter.class, OperationParameter::value));

        return conversionService.convert(value, parameter.type());
    }

}

