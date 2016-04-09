package org.ametiste.routine.dsl.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *  Annotation which indicates that a method parameter should be bound to a operation
 *  parameter.
 * </p>
 *
 * @see org.ametiste.routine.dsl.configuration.task.params.OperationParameterProvider
 * @since 1.1
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationParameter {

    /**
     * <p>
     *  Defines bound parameter name, can't be null or empty.
     * </p>
     *
     * @return bound parameter name, can't be null or empty
     */
    String value();

}
