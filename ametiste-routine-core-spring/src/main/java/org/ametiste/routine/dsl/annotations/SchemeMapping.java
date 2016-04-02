package org.ametiste.routine.dsl.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @since
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SchemeMapping {

    String schemeName() default "";

    Class<?> schemeClass() default Class.class;

}
