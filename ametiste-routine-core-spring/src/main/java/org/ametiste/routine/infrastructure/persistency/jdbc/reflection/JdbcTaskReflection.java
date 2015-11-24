package org.ametiste.routine.infrastructure.persistency.jdbc.reflection;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.infrastructure.persistency.ClosedTaskReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.util.SerializationUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JdbcTaskReflection extends ClosedTaskReflection {

    private static final String saveTaskQuery = "INSERT INTO " +
            "%s (id, state, cr_time, execs_time, co_time, agregate) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String checkExistsTaskQuery =
            "SELECT COUNT(*) FROM %s WHERE id = ?";

    private static final String updateTaskQuery = "UPDATE %s " +
            "SET state=?, execs_time=?, co_time=?, agregate=? WHERE id = ?";

    private static final String saveTaskOperationQuery = "INSERT INTO " +
            "%s (id, task_id) " +
            "VALUES (?, ?)";

    private static final String saveTaskPropertiesQuery = "INSERT INTO " +
            "ame_routine.ame_routine_task_property (task_id, name, value) " +
            "VALUES (?, ?, ?)";

    private final String taskTable;

    private final String operationTable;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private RowMapper<ReflectedTaskData> taskDataRowMapper = new RowMapper<ReflectedTaskData>() {
        @Override
        public ReflectedTaskData mapRow(ResultSet rs, int rowNum) throws SQLException {
            final DefaultLobHandler lobHandler = new DefaultLobHandler();
            final byte[] bytes = lobHandler.getBlobAsBytes(rs, "agregate");
            return ReflectedTaskData.class.cast(SerializationUtils.deserialize(bytes));
        }
    };

    private final JdbcTemplate jdbcTemplate;

    public JdbcTaskReflection(String taskTable, String operationTable, UUID taskId, JdbcTemplate jdbcTemplate) {
        this(taskTable, operationTable, jdbcTemplate);
        flareTaskId(taskId);
    }

    public JdbcTaskReflection(String taskTable, String operationTable, JdbcTemplate jdbcTemplate, ReflectedTaskData reflectedTaskData) {
        this(taskTable, operationTable, jdbcTemplate);
        flareTaskId(reflectedTaskData.taskId);
        this.reflectedTaskData = reflectedTaskData;
    }

    // TODO: split candidate, for read-only reflection
    public JdbcTaskReflection(String taskTable, String operationTable, JdbcTemplate jdbcTemplate) {
        if (jdbcTemplate == null) {
            throw new IllegalArgumentException("jdbcTemplate can't be null");
        }

        if (taskTable == null || taskTable.isEmpty()) {
            throw new IllegalArgumentException("taskTable can't be nul nor empty");
        }

        if (operationTable == null || operationTable.isEmpty()) {
            throw new IllegalArgumentException("operationTable can't be nul nor empty");
        }

        this.taskTable = taskTable;
        this.operationTable = operationTable;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void loadReflection() {
        reflectedTaskData = jdbcTemplate.queryForObject(
            String.format("SELECT agregate FROM %s WHERE id = ?", taskTable),
                taskDataRowMapper, reflectedTaskData.taskId.toString()
        );
    }


    // TODO: hmm, is it right place for this method?
    // TODO: May be I should split this class on two and create base abstract one?
    // TODO: split candidate
    public <T> List<T> loadMultipleReflections(Function<ReflectedTaskData, T> taskReflectionConsumer, int offset, int limit) {

        final List<ReflectedTaskData> reflectedData = jdbcTemplate.query(
                String.format("SELECT agregate FROM %s ORDER BY cr_time DESC LIMIT ?, ?", taskTable),
                taskDataRowMapper, offset, limit
        );

        List<T> processed = reflectedData.stream()
                .map(taskReflectionConsumer::apply).collect(Collectors.toList());

        return processed;
    }

    // TODO: hmm, is it right place for this method?
    // TODO: May be I should split this class on two and create base abstract one?
    // TODO: split candidate
    public <T> List<T> loadMultipleReflections(Function<ReflectedTaskData, T> taskReflectionConsumer, String status, int offset, int limit) {

        final List<ReflectedTaskData> reflectedData = jdbcTemplate.query(
                String.format("SELECT agregate FROM %s WHERE state = ? ORDER BY cr_time DESC LIMIT ?, ?", taskTable),
                taskDataRowMapper, status, offset, limit
        );

        List<T> processed = reflectedData.stream()
                .map(taskReflectionConsumer::apply).collect(Collectors.toList());

        return processed;
    }


    // TODO: hmm, is it right place for this method?
    // TODO: May be I should split this class on two and create base abstract one?
    // TODO: split candidate
    public List<JdbcTaskReflection> loadMultipleReflectionsByState(Task.State state, int offset, int limit) {

        final List<ReflectedTaskData> reflectedData = jdbcTemplate.query(
                String.format("SELECT agregate FROM %s WHERE state = ? ORDER BY cr_time DESC LIMIT ?, ?", taskTable),
                taskDataRowMapper, state.name(), offset, limit
        );

        return reflectedData
                .stream()
                .map((d) -> new JdbcTaskReflection(taskTable, operationTable, jdbcTemplate, d)
                ).collect(Collectors.toList());
    }


    public <T> T processReflection(Function<ReflectedTaskData, T> taskReflectionConsumer) {
        loadReflection();
        return taskReflectionConsumer.apply(reflectedTaskData);
    }

    public void saveReflection() {

        final DefaultLobHandler lobHandler = new DefaultLobHandler();
        final byte[] data = SerializationUtils.serialize(reflectedTaskData);

        // NOTE: there is denormalized representation of task data
        // data that required for search queries stored in the separate fields,
        // actual aggregate data stored within the agregate blob object

        if (isRecordExists()) {

            if (logger.isDebugEnabled()) {
                logger.debug("----");
                logger.debug("Update task  : " + reflectedTaskData.taskId);
                logger.debug("       state : " + reflectedTaskData.state);
                logger.debug("----");
            }

            jdbcTemplate.update(String.format(updateTaskQuery, taskTable),
                    new Object[]{
                            reflectedTaskData.state.name(),
                            Optional.ofNullable(reflectedTaskData.executionStartTime).map(Timestamp::from).orElse(null),
                            Optional.ofNullable(reflectedTaskData.completionTime).map(Timestamp::from).orElse(null),
                            new SqlLobValue(data, lobHandler),
                            reflectedTaskData.taskId.toString()
                    },
                    new int[]{
                            Types.VARCHAR,
                            Types.TIMESTAMP,
                            Types.TIMESTAMP,
                            Types.BLOB,
                            Types.VARCHAR,
                    }
            );

        } else {

            if (logger.isDebugEnabled()) {
                logger.debug("----");
                logger.debug("Save task  : " + reflectedTaskData.taskId);
                logger.debug("       state : " + reflectedTaskData.state);
                logger.debug("----");
            }

            jdbcTemplate.update(String.format(saveTaskQuery, taskTable),
                    new Object[]{
                            reflectedTaskData.taskId.toString(),
                            reflectedTaskData.state.name(),
                            // TODO: Dunno how to store various properties, need to think about
                            // reflectedTaskData.properties.get(BoundItem.PROPERTY_NAME),
                            Optional.ofNullable(reflectedTaskData.creationTime).map(Timestamp::from).orElse(null),
                            Optional.ofNullable(reflectedTaskData.executionStartTime).map(Timestamp::from).orElse(null),
                            Optional.ofNullable(reflectedTaskData.completionTime).map(Timestamp::from).orElse(null),
                            new SqlLobValue(data, lobHandler)
                    },
                    new int[]{
                            Types.VARCHAR,
                            Types.VARCHAR,
                            // Types.VARCHAR,
                            Types.TIMESTAMP,
                            Types.TIMESTAMP,
                            Types.TIMESTAMP,
                            Types.BLOB
                    }
            );

            reflectedTaskData.properties.forEach(
                    (k, v) -> {
                        jdbcTemplate.update(saveTaskPropertiesQuery,
                            new Object[] {
                                    reflectedTaskData.taskId.toString(),
                                    k,
                                    v
                            },
                            new int[] {
                                    Types.VARCHAR,
                                    Types.VARCHAR,
                                    Types.VARCHAR
                            });
                    }
            );

            // NOTE: there is only mapping data to provide search query capabilities,
            // actual operation data stored within the agregate blob

            reflectedTaskData.operationFlare.forEach(o -> {
                jdbcTemplate.update(String.format(saveTaskOperationQuery, operationTable),
                    o.flashId().toString(),
                    reflectedTaskData.taskId.toString()
                );
            });

        }
    }

    private boolean isRecordExists() {
        return jdbcTemplate.queryForObject(String.format(checkExistsTaskQuery, taskTable),
                Integer.class, reflectedTaskData.taskId.toString()) > 0;
    }

}