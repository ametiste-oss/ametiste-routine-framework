package org.ametiste.routine.interfaces.metrics;

import org.ametiste.metrics.MetricsService;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.infrastructure.persistency.jpa.JPATaskDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This {@code metric source} periodically counts stored tasks and provides this value
 * as {@code gauage} metrics named as
 * <ul>
 *     <li>{@value InfoMetrics#STORED_TASKS_COUNT}</li>
 *     <li>{@value InfoMetrics#TERMINATED_TASKS_COUNT}</li>
 *     <li>{@value InfoMetrics#DONE_TASKS_COUNT}</li>
 * </ul>
 * <p>
 * Note, this autoconfigured components depends on {@link JPATaskDataRepository} and
 * will be active only if this repository object is presented within the context.
 * <p>
 * {@code org.ametiste.routine.metrics.source.info.stored-tasks-count.enabled} property may be used
 * to disable this metric source.
 * <p>
 * Also note, in case where tasks count exceed {@value Integer#MAX_VALUE} [int max value],
 * this metric source would provide incorrect information and should be replaced or disabled.
 *
 * @since 0.1.0
 * @see InfoMetrics
 */
@Component
@ConditionalOnProperty(name = "org.ametiste.routine.metrics.source.info.stored-tasks-count.enabled",
        matchIfMissing = true)
// TODO NOTE: @ConditionalOnBean does not work for spring-data driven object, so this check is disabled :[
//      NOTE: @ConditionalOnBean(JPATaskDataRepository.class)
public class TasksCountMetricSource {

    @Autowired(required = false)
    private MetricsService metricsService;

    @Autowired(required = false)
    private JPATaskDataRepository taskDataRepository;

    // TODO: extract as property
    @Scheduled(fixedRate = 30000)
    public void countMetrics() {

        // NOTE: @ConditionalOnBean does not work for spring-data driven object so this check requred :[
        if (taskDataRepository == null || metricsService == null) {
            return;
        }

        // NOTE: just ignoring possible bits lose, in the case of this application these big values does not
        // have sense anyway. See javadoc.
        metricsService.gauge(InfoMetrics.STORED_TASKS_COUNT, (int) taskDataRepository
                .count());

        metricsService.gauge(InfoMetrics.TERMINATED_TASKS_COUNT, (int) taskDataRepository
                .countByState(Task.State.TERMINATED.name()));

        metricsService.gauge(InfoMetrics.DONE_TASKS_COUNT, (int) taskDataRepository
                .countByState(Task.State.DONE.name()));

// TODO: add active tasks count
//        metricsService.gauge(InfoMetrics.DONE_TASKS_COUNT, (int) taskDataRepository
//                .countByState());

    }

}
