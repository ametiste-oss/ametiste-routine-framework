package org.ametiste.routine.configuration;

import org.ametiste.routine.mod.tasklog.domain.TaskLogRepository;
import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.mod.tasklog.infrastructure.persistency.jdbc.JdbcTaskLogRepository;
import org.ametiste.routine.infrastructure.persistency.jdbc.JdbcTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableConfigurationProperties(JdbcTaskRepositoryConfigurationProperties.class)
public class JdbcTaskRepositoryConfiguration {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcTaskRepositoryConfigurationProperties props;

    @Bean
    public TaskLogRepository taskLogRepository() {
        final JdbcTaskLogRepository taskLogRepository = new JdbcTaskLogRepository();
        taskLogRepository.setJdbcTemplate(jdbcTemplate);
        taskLogRepository.setTaskTable(props.getTaskTableName());
        taskLogRepository.setOperationTable(props.getOperationTableName());
        return taskLogRepository;
    }

    @Bean
    public TaskRepository taskRepository() {
        final JdbcTaskRepository jdbcTaskRepository = new JdbcTaskRepository();
        jdbcTaskRepository.setJdbcTemplate(jdbcTemplate);
        jdbcTaskRepository.setTaskTable(props.getTaskTableName());
        jdbcTaskRepository.setOperationTable(props.getOperationTableName());
        return jdbcTaskRepository;
    }

}
