package org.ametiste.routine.mod.tasklog.mod;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.infrastructure.persistency.jpa.data.TaskData;
import org.ametiste.routine.mod.tasklog.infrastructure.persistency.jpa.JPATaskLogDataRepository;
import org.ametiste.routine.mod.tasklog.infrastructure.persistency.jpa.SpringDataTaskLogRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *
 * <p>
 *      Direct {@link TaskLogProtocol} implementation that use {@link JPATaskLogDataRepository}
 *      as backend to provide access to task termination log.
 * </p>
 *
 * @since 0.1.0
 */
public class DirectTaskLogConnection implements TaskLogProtocol {

    private final JPATaskLogDataRepository taskLogDataRepository;

    private class SpecificationLogFilter implements LogFilter {

        private List<Specification<TaskData>> specifications = new ArrayList<>();

        @Override
        public void stateIn(List<Task.State> state) {
              specifications.add(
                  SpringDataTaskLogRepository.TaskDataSpecifications.hasState(state)
              );
        }

        @Override
        public void creationTimeAfter(Instant time) {
            specifications.add(
                SpringDataTaskLogRepository.TaskDataSpecifications.afterCreationTime(time)
            );
        }

        @Override
        public void execStartTimeAfter(Instant time) {
            specifications.add(
                SpringDataTaskLogRepository.TaskDataSpecifications.afterExecStartTime(time)
            );
        }

        @Override
        public void completionTimeAfter(Instant time) {
            specifications.add(
                SpringDataTaskLogRepository.TaskDataSpecifications.afterCompletionTime(time)
            );
        }

        public Optional<Specification<TaskData>> buildSpecification() {

            final Iterator<Specification<TaskData>> iterator =
                    specifications.iterator();

            if (iterator.hasNext()) {
                Specifications<TaskData> specification = Specifications.where(iterator.next());

                while (iterator.hasNext()) {
                    specification = specification.and(iterator.next());
                }

                return Optional.of(specification);
            } else {
                return Optional.empty();
            }

        }

    }

    public DirectTaskLogConnection(JPATaskLogDataRepository taskLogDataRepository) {
        this.taskLogDataRepository = taskLogDataRepository;
    }

    @Override
    public List<UUID> findIdentifiers(Consumer<LogFilter> filter, int offset, int limit) {

        final SpecificationLogFilter specBuilder = new SpecificationLogFilter();
        filter.accept(specBuilder);

        final Optional<Specification<TaskData>> specification = specBuilder.buildSpecification();

        if (!specification.isPresent()) {
            throw new IllegalArgumentException("Atleast one filter predicate must be used.");
        }

        return taskLogDataRepository
            .findAll(specification.get(), new PageRequest(offset, limit))
            .getContent()
            .stream()
            .map(TaskData::getId)
            .collect(Collectors.toList());

    }

}
