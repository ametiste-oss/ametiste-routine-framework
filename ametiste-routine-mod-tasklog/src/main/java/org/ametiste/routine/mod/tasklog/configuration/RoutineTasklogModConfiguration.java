package org.ametiste.routine.mod.tasklog.configuration;

import org.ametiste.routine.mod.tasklog.domain.TaskLogRepository;
import org.ametiste.routine.mod.tasklog.infrastructure.persistency.jpa.JPATaskLogDataRepository;
import org.ametiste.routine.mod.tasklog.infrastructure.persistency.jpa.SpringDataTaskLogRepository;
import org.ametiste.routine.mod.tasklog.interfaces.web.TaskLogController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @since
 */
@Configuration
@ComponentScan(basePackageClasses = TaskLogController.class)
public class RoutineTasklogModConfiguration {

    @Autowired
    private JPATaskLogDataRepository jpaTaskLogDataRepository;

    @Bean
    public TaskLogRepository taskLogRepository() {
        return new SpringDataTaskLogRepository(jpaTaskLogDataRepository);
    }

}
