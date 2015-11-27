package org.ametiste.routine.configuration;

import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.domain.scheme.TaskSchemeRepository;
import org.ametiste.routine.infrastructure.persistency.memory.InMemoryTaskSchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 *
 * @since
 */
@Configuration
public class TaskSchemeRepositoryConfiguration {

    @Autowired(required = false)
    private Map<String, TaskScheme> schemes;

    @Bean
    public TaskSchemeRepository taskSchemeRepository() {
        return new InMemoryTaskSchemeRepository(schemes);
    }

}
