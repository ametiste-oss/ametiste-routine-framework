package org.ametiste.routine.infrastructure.metrics;

import org.ametiste.metrics.annotations.markers.MetricsInterface;

/**
 *
 * @since
 */
@MetricsInterface
public interface InfoMetrics {

    String STORED_TASKS_COUNT = "info.stored-tasks-count";

    String TERMINATED_TASKS_COUNT = "info.terminated-tasks-count";

    String DONE_TASKS_COUNT = "info.done-tasks-count";

}
