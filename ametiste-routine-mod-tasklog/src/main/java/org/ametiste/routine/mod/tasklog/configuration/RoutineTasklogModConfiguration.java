package org.ametiste.routine.mod.tasklog.configuration;

import org.ametiste.routine.configuration.JdbcTaskRepositoryConfigurationProperties;
import org.ametiste.routine.mod.tasklog.domain.TaskLogRepository;
import org.ametiste.routine.mod.tasklog.infrastructure.persistency.jdbc.JdbcTaskLogRepository;
import org.ametiste.routine.mod.tasklog.interfaces.web.TaskLogController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @since
 */
@Configuration
@ComponentScan(basePackageClasses = TaskLogController.class)
public class RoutineTasklogModConfiguration {

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

}
