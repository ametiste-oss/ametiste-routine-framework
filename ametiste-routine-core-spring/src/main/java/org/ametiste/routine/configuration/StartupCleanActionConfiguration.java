package org.ametiste.routine.configuration;

import org.ametiste.routine.application.action.StartupCleanAction;
import org.ametiste.routine.domain.task.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 *
 * @since 0.1.0
 */
@Configuration
public class StartupCleanActionConfiguration implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private TaskRepository taskRepository;

    @Bean
    public StartupCleanAction startupCleanAction() {
        return new StartupCleanAction(taskRepository);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        startupCleanAction().run();
    }

}
