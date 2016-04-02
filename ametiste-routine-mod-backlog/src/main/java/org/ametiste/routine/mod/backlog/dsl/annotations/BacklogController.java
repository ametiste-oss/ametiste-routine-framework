package org.ametiste.routine.mod.backlog.dsl.annotations;

import org.ametiste.routine.mod.backlog.application.service.BacklogRenewConstraint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @since
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
@Component
@Inherited
public @interface BacklogController {

}
