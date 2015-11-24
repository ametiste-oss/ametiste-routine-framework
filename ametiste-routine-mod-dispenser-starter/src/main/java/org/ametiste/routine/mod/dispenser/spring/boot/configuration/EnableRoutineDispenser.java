package org.ametiste.routine.mod.dispenser.spring.boot.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 *
 * @since
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import(RoutineDispenserModConfiguration.class)
public @interface EnableRoutineDispenser {

}
