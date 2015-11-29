package org.ametiste.routine.mod.tasklog.infrastructure.persistency.jdbc;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.mod.tasklog.domain.NoticeEntry;
import org.ametiste.routine.mod.tasklog.domain.OperationLog;
import org.ametiste.routine.mod.tasklog.domain.TaskLogEntry;
import org.ametiste.routine.mod.tasklog.domain.TaskLogRepository;
import org.ametiste.routine.domain.task.notices.Notice;
import org.ametiste.routine.infrastructure.persistency.ClosedTaskReflection;
import org.ametiste.routine.infrastructure.persistency.jdbc.reflection.JdbcTaskReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @since
 */
public class JdbcTaskLogRepository implements TaskLogRepository {

    private final String taskPropertiesTable = "ame_routine_task_property";
    private JdbcTemplate jdbcTemplate;

    private final Logger logger = LoggerFactory.getLogger(getClass());

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
    public long countActiveTasks() {

        final long tasksCount = jdbcTemplate.queryForObject(
                String.format("SELECT COUNT(*) FROM %s WHERE state in ('EXECUTION', 'PENDING')", taskTable), Long.class);

        return tasksCount;
    }

    @Override
    public List<UUID> findNewTasks(long appendCount) {

        final List<String> newTasks = jdbcTemplate.queryForList(
                String.format("SELECT id FROM %s WHERE state = 'NEW' ORDER BY cr_time DESC LIMIT ?", taskTable), String.class, appendCount);

        return newTasks.stream()
                .map(UUID::fromString)
                .collect(Collectors.toList());
    }

    @Override
    public void saveTaskLog(TaskLogEntry taskLogEntry) {

    }

    @Override
    public List<TaskLogEntry> findEntries() {
        return new JdbcTaskReflection(taskTable, operationTable, jdbcTemplate)
                .loadMultipleReflections(this::processReflectedEntry, 0, 100);
    }

    @Override
    public TaskLogEntry findTaskLog(UUID taskId) {
        final JdbcTaskReflection jdbcTaskReflection = new JdbcTaskReflection(taskTable, operationTable, taskId, jdbcTemplate);
        return jdbcTaskReflection.processReflection(this::processReflectedEntry);

    }

    @Override
    public List<UUID> findActiveTasksAfterDate(Instant timePoint) {

        final List<String> tasks = jdbcTemplate.queryForList(
                String.format("SELECT id FROM %s " +
                        "WHERE execs_time < ? AND state in ('EXECUTION', 'PENDING')", taskTable),
                String.class,
                Timestamp.from(timePoint)
        );

        return tasks.stream()
                .map(UUID::fromString)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskLogEntry> findEntries(String byStatus, int offset, int limit) {
        return new JdbcTaskReflection(taskTable, operationTable, jdbcTemplate)
                .loadMultipleReflections(this::processReflectedEntry, byStatus, offset, limit);
    }

    @Override
    public List<TaskLogEntry> findEntries(List<Task.State> states, int offset, int limit) {
        final String findQuery = String.format("SELECT agregate " +
                        "FROM " + taskTable +
                        " WHERE " +
                        "   %s " +
                        " ORDER BY cr_time DESC" +
                        " LIMIT %s, %s ",
                createStateFilter(states),
                offset,
                limit);

        return new JdbcTaskReflection(taskTable, operationTable, jdbcTemplate)
                .loadMultipleReflectionsAs(
                        findQuery,
                        this::processReflectedEntry
                );
    }

    @Override
    public List<TaskLogEntry> findEntries(List<Task.State> states, List<TaskProperty> properties, int offset, int limit) {

        final String findQuery = String.format("SELECT agregate " +
                "FROM " + taskTable + " JOIN " + taskPropertiesTable +
                " ON " +
                "   " + taskTable + ".id = " + taskPropertiesTable + ".task_id " +
                " WHERE " +
                "   %s " +
                "   %s " +
                " GROUP BY id " +
                " HAVING COUNT(id) = %s" +
                " ORDER BY cr_time DESC" +
                " LIMIT %s, %s ",
                    createStateFilter(states),
                    (states.size() > 0 ? "AND " : "") + createPropertiesFilter(properties),
                    properties.size(),
                    offset,
                    limit);

        return new JdbcTaskReflection(taskTable, operationTable, jdbcTemplate)
                .loadMultipleReflectionsAs(
                        findQuery,
                        this::processReflectedEntry
                );
    }

    @Override
    public int countEntriesByStatus(String byStatus) {

        final int tasksCount = jdbcTemplate.queryForObject(
                String.format("SELECT COUNT(id) FROM %s WHERE state = ?", taskTable), Integer.class, byStatus);

        return tasksCount;

    }

    @Override
    public int countByTaskState(Task.State[] states, TaskProperty[] properties) {

        final String countQuery = String.format("SELECT COUNT(*) FROM ( " +
                "SELECT id FROM " + taskTable + " JOIN " + taskPropertiesTable +
                " ON " +
                "   " + taskTable + ".id = " + taskPropertiesTable + ".task_id " +
                " WHERE " +
                "   %s "  +
                "   %s " +
                " GROUP BY id " +
                " HAVING COUNT(id) = %s )",
                createStateFilter(Arrays.asList(states)),
                (states.length > 0 ? "AND " : "") + createPropertiesFilter(Arrays.asList(properties)),
                properties.length
        );

        return jdbcTemplate.queryForObject(countQuery, Integer.class);
    }

    @Override
    public int countByTaskState(Task.State[] states) {
        final int tasksCount = jdbcTemplate.queryForObject(
                String.format("SELECT COUNT(id) FROM %s WHERE state in (%s)", taskTable, createStateFilter(Arrays.asList(states))),
                Integer.class);
        return tasksCount;
    }

    private NoticeEntry createNoticeEntry(ClosedTaskReflection.ReflectedOperationNoticeData notice) {
        return new NoticeEntry(notice.creationTime, notice.text);
    }

    private NoticeEntry createNoticeEntry(ClosedTaskReflection.ReflectedTaskNoticeData notice) {
        return new NoticeEntry(notice.creationTime, notice.text);
    }

    private TaskLogEntry processReflectedEntry(ClosedTaskReflection.ReflectedTaskData reflectedData) {
        return new TaskLogEntry(
                reflectedData.id,
                reflectedData.creationTime,
                reflectedData.executionStartTime,
                reflectedData.completionTime,
                reflectedData.notices.stream()
                        .map(this::createNoticeEntry)
                        .collect(Collectors.toList()),
                reflectedData.state.name(),
                reflectedData.properties.stream()
                        .collect(Collectors.toMap((p) -> p.name, (p) -> p.value)),
                reflectedData.operationData.stream()
                        .map((x) -> {
                            return new OperationLog(
                                    x.id,
                                    x.label,
                                    x.state,
                                    x.notices.stream()
                                            .map(this::createNoticeEntry)
                                            .collect(Collectors.toList()));
                        })
                        .collect(Collectors.toList())
        );
    }

    private String createPropertiesFilter(List<TaskProperty> properties) {

        if (properties.isEmpty()) {
            return "";
        }

        return taskPropertiesTable + ".name in (" + String.join(",", properties
                .stream().map(p -> "'" + p.name() + "'")
                .collect(Collectors.toList())
        ) + ") AND " +
                taskPropertiesTable + ".value in (" + String.join(",", properties
                .stream().map(p -> "'" + p.value() + "'")
                .collect(Collectors.toList())
        ) + ")";
    }

    private String createStateFilter(List<Task.State> states) {

        if (states.isEmpty()) {
            return "";
        }

        return "state in (" + String.join(",", states
                .stream()
                .map(state -> "'" + state.name() + "'")
                .collect(Collectors.toList()))
        + ")";
    }

}
