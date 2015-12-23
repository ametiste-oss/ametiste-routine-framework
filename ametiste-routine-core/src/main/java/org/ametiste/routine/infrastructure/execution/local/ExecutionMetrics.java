package org.ametiste.routine.infrastructure.execution.local;

import org.ametiste.metrics.annotations.markers.MetricsInterface;

/**
 *
 * @since 0.1.0
 */
@MetricsInterface
public interface ExecutionMetrics {

    String GATEWAY_EXECUTION_TIMING = "core.infrastructure.execution.local-gateway.timing";

}
