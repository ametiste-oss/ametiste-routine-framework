package org.ametiste.routine.configuration;

import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.infrastructure.persistency.sdata.JPATaskDataRepository;
import org.ametiste.routine.infrastructure.persistency.sdata.SpringDataTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableConfigurationProperties(JdbcTaskRepositoryConfigurationProperties.class)
public class JdbcTaskRepositoryConfiguration {

    @Autowired
    private JPATaskDataRepository jpaTaskDataRepository;

    @Bean
    public TaskRepository taskRepository() {
        return new SpringDataTaskRepository(jpaTaskDataRepository);
    }

}
