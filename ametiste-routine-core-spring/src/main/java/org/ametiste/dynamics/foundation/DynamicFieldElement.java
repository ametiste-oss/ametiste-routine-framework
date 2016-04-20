package org.ametiste.dynamics.foundation;

import org.ametiste.dynamics.foundation.feature.ReferenceFeature;

import java.lang.reflect.Field;

public class DynamicFieldElement extends AnnotatedObjectElement<Field> {
    public DynamicFieldElement(Field field) {
        super(field, new GeneralDelegate(field.getName(), new ReferenceFeature<>(field.getType())));
    }
}
