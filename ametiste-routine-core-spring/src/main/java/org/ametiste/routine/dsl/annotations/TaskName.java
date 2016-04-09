package org.ametiste.routine.dsl.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *  Annotation which indicates that a method parameter should be bound to the task
 *  name value.
 * </p>
 *
 * @see org.ametiste.routine.dsl.configuration.task.params.OperationIdProvider
 * @since 1.1
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TaskName {
}
