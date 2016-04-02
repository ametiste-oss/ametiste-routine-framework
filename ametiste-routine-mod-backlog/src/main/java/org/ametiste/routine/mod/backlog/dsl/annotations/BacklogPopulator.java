package org.ametiste.routine.mod.backlog.dsl.annotations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 *
 * @since
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface BacklogPopulator { }
