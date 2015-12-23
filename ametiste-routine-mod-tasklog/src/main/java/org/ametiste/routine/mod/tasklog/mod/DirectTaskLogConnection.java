package org.ametiste.routine.mod.tasklog.mod;

import org.ametiste.routine.infrastructure.persistency.jpa.JPATaskFilter;
import org.ametiste.routine.infrastructure.persistency.jpa.data.TaskData;
import org.ametiste.routine.mod.tasklog.infrastructure.persistency.jpa.JPATaskLogDataRepository;
import org.ametiste.routine.sdk.domain.TaskFilter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *
 * <p>
 *      Direct {@link TaskLogProtocol} implementation that use {@link JPATaskLogDataRepository}
 *      as backend to provide access to tasks execution log.
 * </p>
 *
 * @since 0.1.0
 */
public class DirectTaskLogConnection implements TaskLogProtocol {

    private final JPATaskLogDataRepository taskLogDataRepository;

    public DirectTaskLogConnection(JPATaskLogDataRepository taskLogDataRepository) {
        this.taskLogDataRepository = taskLogDataRepository;
    }

    @Override
    public List<UUID> findIdentifiers(Consumer<TaskFilter> filterBuilder, int offset, int limit) {
        return taskLogDataRepository
            .findAll(JPATaskFilter.buildBy(filterBuilder), new PageRequest(offset, limit))
            .getContent()
            .stream()
            .map(TaskData::getId)
            .collect(Collectors.toList());
    }

}
