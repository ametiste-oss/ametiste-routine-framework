package org.ametiste.routine.infrastructure.persistency.jdbc;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.infrastructure.persistency.jdbc.reflection.JdbcTaskReflection;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @since
 */
public class JdbcTaskRepository implements TaskRepository {

    private JdbcTemplate jdbcTemplate;

    private String taskTable;

    private String operationTable;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setTaskTable(String taskTable) {
        this.taskTable = taskTable;
    }

    public void setOperationTable(String operationTable) {
        this.operationTable = operationTable;
    }


    @Override
    public Task findTask(UUID taskId) {
        final JdbcTaskReflection jdbcTaskReflection =
                new JdbcTaskReflection(taskTable, operationTable, taskId, jdbcTemplate);
        jdbcTaskReflection.loadReflection();
        return new Task(jdbcTaskReflection);
    }

    @Override
    public List<Task> findTasksByState(Task.State state, int limit) {

        final List<JdbcTaskReflection> reflectedTaskDatas =
                new JdbcTaskReflection(taskTable, operationTable, jdbcTemplate)
                .loadMultipleReflectionsByState(state, 0, limit);

        return reflectedTaskDatas.stream().map(
                (r) -> new Task(r)
        ).collect(Collectors.toList());
    }

    @Override
    public void saveTask(Task task) {
        final JdbcTaskReflection jdbcTaskReflection = new JdbcTaskReflection(taskTable, operationTable, task.entityId(), jdbcTemplate);
        task.reflectAs(jdbcTaskReflection);
        jdbcTaskReflection.saveReflection();
    }

    @Override
    public Task findTaskByOperationId(UUID operationId) {

        final String taskId = jdbcTemplate.queryForObject(
            String.format("SELECT task_id FROM %s WHERE id = ? FOR UPDATE", operationTable), String.class, operationId.toString()
        );

        if (taskId == null) {
            throw new RuntimeException("Task not found by operation id: " + operationId.toString());
        }

        final JdbcTaskReflection jdbcTaskReflection =
                new JdbcTaskReflection(taskTable, operationTable, UUID.fromString(taskId), jdbcTemplate);

        jdbcTaskReflection.loadReflection();

        return new Task(jdbcTaskReflection);
    }

}
