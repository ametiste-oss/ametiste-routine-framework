package org.ametiste.routine.configuration;

import org.ametiste.routine.RoutineCoreSpring;
import org.ametiste.routine.application.action.StartupCleanupAction;
import org.ametiste.routine.domain.task.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @since 0.1.0
 */
@Configuration
@ConditionalOnProperty(prefix = RoutineCoreSpring.PROPS_PREFIX,
    name = "startup.cleanup.enabled", matchIfMissing = true)
public class StartupCleanActionConfiguration implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private TaskRepository taskRepository;

    @Bean
    public StartupCleanupAction startupCleanAction() {
        return new StartupCleanupAction(taskRepository);
    }

    // TODO: move it to GTE somehow, singletone action or something like this
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        startupCleanAction().run();
    }

}
