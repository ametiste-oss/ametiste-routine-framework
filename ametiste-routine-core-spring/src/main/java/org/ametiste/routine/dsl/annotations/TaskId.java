package org.ametiste.routine.dsl.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *  Annotation which indicates that a method parameter should be bound to the task
 *  identifier.
 * </p>
 *
 * @see org.ametiste.routine.dsl.configuration.task.params.TaskIdProvider
 * @since 1.1
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TaskId {
}
