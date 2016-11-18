package org.ametiste.routine.dsl.annotations;

import org.ametiste.dynamics.Surge;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Marks {@link Surge} that used to provide method parameters values.
 *
 * <p>
 *     This is {@link Qualifier} annotation, it is used to depict concrete provider types
 *     and reduce runtime effort of dsl mechanisms.
 *
 * @since 1.1
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
@Qualifier
@Inherited
public @interface ParamValueProvider {
}
