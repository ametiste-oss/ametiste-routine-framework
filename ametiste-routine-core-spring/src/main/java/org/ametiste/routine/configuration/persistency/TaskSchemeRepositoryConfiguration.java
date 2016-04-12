package org.ametiste.routine.configuration.persistency;

import org.ametiste.routine.domain.scheme.OperationScheme;
import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.domain.scheme.SchemeRepository;
import org.ametiste.routine.infrastructure.persistency.memory.InMemorySchemeRepository;
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
    private Map<String, TaskScheme> taskSchemes;

    @Autowired(required = false)
    private Map<String, OperationScheme> opSchemes;

    @Bean
    public SchemeRepository taskSchemeRepository() {
        return new InMemorySchemeRepository(taskSchemes, opSchemes);
    }

}
