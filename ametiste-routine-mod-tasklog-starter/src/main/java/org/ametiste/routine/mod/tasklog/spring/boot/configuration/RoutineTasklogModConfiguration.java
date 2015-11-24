package org.ametiste.routine.mod.tasklog.spring.boot.configuration;

import org.ametiste.routine.mod.tasklog.interfaces.web.TaskLogController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @since
 */
@Configuration
@ComponentScan(basePackageClasses = TaskLogController.class)
public class RoutineTasklogModConfiguration {
}
