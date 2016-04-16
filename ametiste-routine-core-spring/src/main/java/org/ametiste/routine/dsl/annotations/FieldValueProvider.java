package org.ametiste.routine.dsl.annotations;

import org.ametiste.routine.dsl.application.RuntimeElementValueProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Marks {@link RuntimeElementValueProvider} that used to provide field values.
 *
 * <p>
 *     This is {@link Qualifier} annotation, it is used to depict concrete provider types
 *     in a way to reduce runtime effort of dsl mechanisms.
 *
 * @since 1.1
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
@Qualifier
@Inherited
public @interface FieldValueProvider {
}
