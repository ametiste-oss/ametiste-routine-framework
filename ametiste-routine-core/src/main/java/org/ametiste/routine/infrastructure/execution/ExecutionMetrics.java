package org.ametiste.routine.infrastructure.execution;

import org.ametiste.metrics.annotations.markers.MetricsInterface;

/**
 *
 * @since 0.1.0
 */
@MetricsInterface
public interface ExecutionMetrics {

    String TASK_EXECUTION_TIMING = "core.infrastructure.execution.local-gateway.timing";

}
