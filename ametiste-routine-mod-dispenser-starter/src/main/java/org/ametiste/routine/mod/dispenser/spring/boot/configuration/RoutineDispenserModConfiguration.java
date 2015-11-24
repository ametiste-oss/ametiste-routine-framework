package org.ametiste.routine.mod.dispenser.spring.boot.configuration;

import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.mod.dispenser.application.service.DefaultOperationReservationService;
import org.ametiste.routine.mod.dispenser.application.service.OperationReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @since
 */
@Configuration
@EnableConfigurationProperties(RoutineDispenserModProperties.class)
@ComponentScan("org.ametiste.routine.mod.dispenser.interfaces.web")
public class RoutineDispenserModConfiguration {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private RoutineDispenserModProperties props;

    @Bean
    public OperationReservationService taskReservationService() {
        return new DefaultOperationReservationService(taskRepository, props.getReservationLimit());
    }

}
