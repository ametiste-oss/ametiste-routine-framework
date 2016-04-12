package org.ametiste.routine.configuration.persistency;

import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.infrastructure.persistency.jpa.JPATaskDataRepository;
import org.ametiste.routine.infrastructure.persistency.jpa.SpringDataTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RepositoryConfigurationProperties.class)
public class JPATaskRepositoryConfiguration {

    @Autowired
    private JPATaskDataRepository jpaTaskDataRepository;

    @Bean
    public TaskRepository taskRepository() {
        return new SpringDataTaskRepository(jpaTaskDataRepository);
    }

}
