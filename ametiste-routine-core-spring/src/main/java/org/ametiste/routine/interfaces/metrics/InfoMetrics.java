package org.ametiste.routine.interfaces.metrics;

import org.ametiste.metrics.annotations.markers.MetricsInterface;

/**
 *
 * @since
 */
@MetricsInterface
public interface InfoMetrics {

    String STORED_TASKS_COUNT = "core.info.stored-tasks-count";

    String TERMINATED_TASKS_COUNT = "core.info.terminated-tasks-count";

    String DONE_TASKS_COUNT = "core.info.done-tasks-count";

    String ACTIVE_TASKS_COUNT = "core.info.active-tasks-count";

}
