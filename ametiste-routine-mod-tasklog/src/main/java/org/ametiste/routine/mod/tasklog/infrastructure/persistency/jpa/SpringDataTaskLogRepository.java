package org.ametiste.routine.mod.tasklog.infrastructure.persistency.jpa;

import org.ametiste.metrics.annotations.Timeable;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.infrastructure.persistency.jpa.data.*;
import org.ametiste.routine.mod.tasklog.domain.NoticeEntry;
import org.ametiste.routine.mod.tasklog.domain.OperationLog;
import org.ametiste.routine.mod.tasklog.domain.TaskLogEntry;
import org.ametiste.routine.mod.tasklog.domain.TaskLogRepository;
import org.ametiste.routine.mod.tasklog.infrastructure.persistency.PersistencyMetrics;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 *
 * @since
 */
public class SpringDataTaskLogRepository implements TaskLogRepository {

    private static class TaskDataSpecifications {

        public static Specification<TaskData> hasScheme(String schemeId) {
            return (root, query, cb) -> {
                return cb.equal(root.get(TaskData_.schemeId), schemeId);
            };
        }

        public static Specification<TaskData> hasCreator(String creatorId) {
            return (root, query, cb) -> {
                return cb.equal(root.get(TaskData_.creatorId), creatorId);
            };
        }

        public static Specification<TaskData> hasProperty(TaskProperty taskProperty) {
            return (root, query, cb) -> {

                final ListJoin<TaskData, TaskPropertyData> join =
                        root.join(TaskData_.properties);

                final Path<String> stringPath = root.get(TaskData_.state);

                return cb.and(
                    cb.equal(join.get(TaskPropertyData_.name), taskProperty.name()),
                    cb.equal(join.get(TaskPropertyData_.value), taskProperty.value())
                );
            };
        }

        public static Specification<TaskData> hasState(final List<Task.State> states) {
            return (root, query, cb) -> {
                return root.get(TaskData_.state)
                        .in(states.stream()
                                .map(Task.State::name)
                                .collect(Collectors.toList())
                        );
            };
        }

    }

    // TODO: extract me
    private static class SpecificationAccumulator<T> implements Supplier<Specification<T>>, Specification<T> {

        private Specifications<T> accumulator;

        public SpecificationAccumulator(Specifications<T> accumulator) {
            this.accumulator = accumulator;
        }

        public void and(Specification<T> specification) {
            this.accumulator = this.accumulator.and(specification);
        }

        public void or(Specification<T> specification) {
            this.accumulator = this.accumulator.or(specification);
        }

        @Override
        public Specifications<T> get() {
            return accumulator;
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return accumulator.toPredicate(root, query, cb);
        }
    }

    private final JPATaskLogDataRepository jpaTaskLogDataRepository;

    public SpringDataTaskLogRepository(JPATaskLogDataRepository jpaTaskLogDataRepository) {
        this.jpaTaskLogDataRepository = jpaTaskLogDataRepository;
    }

    @Override
    @Transactional
    @Timeable(name = PersistencyMetrics.COUNT_ACTIVE_TASKS_TIMING)
    public long countActiveTasks() {
        return jpaTaskLogDataRepository.countTaskByStateIn(
            statesToString(Task.State.activeStatesList)
        );
    }

    @Override
    @Transactional
    @Timeable(name = PersistencyMetrics.FIND_ENTRIES_TIMING)
    public List<TaskLogEntry> findEntries() {
        throw new UnsupportedOperationException("Reimplemnt it as limited query.");
    }

    @Override
    @Transactional
    @Timeable(name = PersistencyMetrics.FIND_ENTRY_TIMING)
    public TaskLogEntry findTaskLog(UUID taskId) {
        return processReflectedEntry(
            jpaTaskLogDataRepository.findOne(taskId)
        );
    }

    @Override
    @Transactional
    @Timeable(name = PersistencyMetrics.FIND_ACTIVE_AFTER_DATE_TIMING)
    public List<UUID> findActiveTasksAfterDate(Instant timePoint) {
        return null;
    }

    @Override
    @Transactional
    @Timeable(name = PersistencyMetrics.FIND_BY_STATE_TIMING)
    public List<TaskLogEntry> findEntries(List<Task.State> states, int offset, int limit) {

        final Specifications<TaskData> accumulator =
                Specifications.where(TaskDataSpecifications.hasState(states));

        return jpaTaskLogDataRepository.findAll(accumulator, new PageRequest(offset, limit))
                .getContent()
                .stream()
                .map(this::processReflectedEntry)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Timeable(name = PersistencyMetrics.FIND_BY_STATE_AND_PROPS_TIMING)
    public List<TaskLogEntry> findEntries(List<Task.State> states, List<TaskProperty> properties, int offset, int limit) {
        return jpaTaskLogDataRepository.findAll(createStateAndPropsSpec(states, properties), new PageRequest(offset, limit))
                .getContent()
                .stream()
                .map(this::processReflectedEntry)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Timeable(name = PersistencyMetrics.COUNT_BY_STATE_TIMING)
    public int countByState(String byStatus) {
        return jpaTaskLogDataRepository.countTaskByState(byStatus);
    }

    @Override
    @Transactional
    @Timeable(name = PersistencyMetrics.COUNT_BY_STATE_AND_PROP_TIMING)
    public long countByTaskState(List<Task.State> states, List<TaskProperty> properties) {
        return jpaTaskLogDataRepository.count(createStateAndPropsSpec(states, properties));
    }


    /* Helper methods section */

    private List<String> statesToString(List<Task.State> states) {
        return states.stream().map(Task.State::name).collect(Collectors.toList());
    }

    private TaskPropertyData propsToData(TaskProperty p) {
        final TaskPropertyData data = new TaskPropertyData();
        data.name = p.name();
        data.value = p.value();
        return data;
    }

    private NoticeEntry createNoticeEntry(OperationNoticeData notice) {
        return new NoticeEntry(notice.creationTime, notice.text);
    }

    private NoticeEntry createNoticeEntry(TaskNoticeData notice) {
        return new NoticeEntry(notice.creationTime, notice.text);
    }

    private TaskLogEntry processReflectedEntry(TaskData reflectedData) {
        return new TaskLogEntry(
                reflectedData.id,
                reflectedData.creationTime,
                reflectedData.executionStartTime,
                reflectedData.completionTime,
                reflectedData.notices.stream()
                        .map(this::createNoticeEntry)
                        .collect(Collectors.toList()),
                reflectedData.state,
                reflectedData.properties.stream()               ya
                        .collect(Collectors.toMap(p -> p.name, p -> p.value)),
                reflectedData.operationData.stream()
                        .map((x) -> {
                            return new OperationLog(
                                    x.id,
                                    x.label,
                                    x.state,
                                    x.notices.stream()
                                            .map(this::createNoticeEntry)
                                            .collect(Collectors.toList()),
                                    x.properties.stream()
                                            .collect(Collectors.toMap(v -> v.name, v -> v.value)));
                        })
                        .collect(Collectors.toList())
        );
    }

    private SpecificationAccumulator<TaskData> createStateAndPropsSpec(List<Task.State> states, List<TaskProperty> properties) {

        final SpecificationAccumulator<TaskData> accumulator =
                new SpecificationAccumulator<>(Specifications.where(TaskDataSpecifications.hasState(states)));

        properties.stream()
                .map(this::createPropSpec)
                .forEach(accumulator::and);

        return accumulator;
    }

    private Specification<TaskData> createPropSpec(TaskProperty taskProperty) {

        if (taskProperty.name().equals(Task.CREATOR_PROPERTY_NAME)) {
            return TaskDataSpecifications.hasCreator(taskProperty.value());
        }

        if (taskProperty.name().equals(Task.SCHEME_PROPERTY_NAME)) {
            return TaskDataSpecifications.hasScheme(taskProperty.value());
        }

        return TaskDataSpecifications.hasProperty(taskProperty);
    }

}
