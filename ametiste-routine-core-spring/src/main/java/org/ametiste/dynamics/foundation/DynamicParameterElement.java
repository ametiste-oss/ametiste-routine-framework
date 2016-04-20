package org.ametiste.dynamics.foundation;

import org.ametiste.dynamics.foundation.feature.ReferenceFeature;

import java.lang.reflect.Parameter;

public class DynamicParameterElement extends AnnotatedObjectElement<Parameter> {

    public DynamicParameterElement(Parameter parameter) {
        super(parameter,
            new GeneralDelegate(parameter.getName(), new ReferenceFeature<>(parameter.getType()))
        );
    }

}
