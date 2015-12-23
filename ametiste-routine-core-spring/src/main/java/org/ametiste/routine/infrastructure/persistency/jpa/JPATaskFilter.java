package org.ametiste.routine.infrastructure.persistency.jpa;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.infrastructure.persistency.jpa.data.TaskData;
import org.ametiste.routine.sdk.domain.TaskFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class JPATaskFilter implements TaskFilter {

    private List<Specification<TaskData>> specifications = new ArrayList<>();

    public static final Specification<TaskData> buildBy(Consumer<TaskFilter> builder) {

        final JPATaskFilter jpaTaskFilter = new JPATaskFilter();
        builder.accept(jpaTaskFilter);

        return jpaTaskFilter.buildSpecification().orElseThrow(
            () -> new IllegalArgumentException("Atleast one filter predicate must be used.")
        );
    }

    @Override
    public void stateIn(List<String> state) {
          specifications.add(
              TaskDataSpecifications.hasState(
                      state.stream().map(Task.State::valueOf).collect(Collectors.toList())
              )
          );
    }

    @Override
    public void creationTimeAfter(Instant time) {
        specifications.add(
            TaskDataSpecifications.afterCreationTime(time)
        );
    }

    @Override
    public void execStartTimeAfter(Instant time) {
        specifications.add(
            TaskDataSpecifications.afterExecStartTime(time)
        );
    }

    @Override
    public void completionTimeAfter(Instant time) {
        specifications.add(
            TaskDataSpecifications.afterCompletionTime(time)
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