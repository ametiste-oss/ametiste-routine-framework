package org.ametiste.routine.infrastructure.persistency.jpa;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.infrastructure.persistency.jpa.data.TaskData;
import org.ametiste.routine.infrastructure.persistency.jpa.data.TaskData_;
import org.ametiste.routine.infrastructure.persistency.jpa.data.TaskPropertyData;
import org.ametiste.routine.infrastructure.persistency.jpa.data.TaskPropertyData_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Path;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TaskDataSpecifications {

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

    public static Specification<TaskData> afterCreationTime(Instant crTime) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder
                    .lessThan(root.get(TaskData_.creationTime), Date.from(crTime));
        };
    }

    public static Specification<TaskData> afterExecStartTime(Instant execTime) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder
                    .lessThan(root.get(TaskData_.executionStartTime), Date.from(execTime));
        };
    }

    public static Specification<TaskData> afterCompletionTime(Instant coTime) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.
                    lessThan(root.get(TaskData_.completionTime), Date.from(coTime));
        };
    }
}