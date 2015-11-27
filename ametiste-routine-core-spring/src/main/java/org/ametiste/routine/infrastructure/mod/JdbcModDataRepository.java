package org.ametiste.routine.infrastructure.mod;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

/**
 *
 * @since
 */
public class JdbcModDataRepository implements ModDataRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcModDataRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveModProperty(String modId, String name, String value) {
        jdbcTemplate.update(
                "INSERT INTO ame_routine.ame_routine_mod_property (mod_id, name, value) " +
                        "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE value = VALUES(value)",
                modId, name, value
        );
    }

    @Override
    public Optional<String> loadModProperty(String modId, String name) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(
                    "SELECT value FROM ame_routine.ame_routine_mod_property " +
                            "WHERE mod_id = ? AND name = ?", String.class, modId, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

}
