package org.ametiste.routine.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @since
 */
@ConfigurationProperties
public class JdbcTaskRepositoryConfigurationProperties {

    private String taskTableName = "ame_routine_task";

    private String operationTableName = "ame_routine_task_operation";

    public void setTaskTableName(String taskTableName) {
        this.taskTableName = taskTableName;
    }

    public String getTaskTableName() {
        return taskTableName;
    }

    public String getOperationTableName() {
        return operationTableName;
    }

    public void setOperationTableName(String operationTableName) {
        this.operationTableName = operationTableName;
    }
}
