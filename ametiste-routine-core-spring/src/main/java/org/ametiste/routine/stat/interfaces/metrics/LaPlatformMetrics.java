package org.ametiste.routine.stat.interfaces.metrics;

import org.ametiste.metrics.annotations.markers.MetricsInterface;
import org.ametiste.routine.infrastructure.laplatform.LaPlatformStatsService;

/**
 * Describes {@link LaPlatformStatsService} data as {@link MetricsInterface}.
 *
 * @see LaPlatformStatsMetricsSource
 * @since 1.1
 */
@MetricsInterface
public interface LaPlatformMetrics {

    /**
     * Metric that shows count of a named protocol instances created at the application runtime.
     *
     * <p><i>{protocol}</i> placeholder will be replaced by a concrete protocol name at the runtime</p>
     *
     * @see #protocolsCreatedCountMetric(String)
     */
    String PROTOCOLS_CREATED_COUNT = "platform.protocols.{protocol}.created";

    /**
     * Composes concrete {@link #PROTOCOLS_CREATED_COUNT} metric name using the given protocol name
     * to fulfill {@code {protocol}} placeholder.
     *
     * @param protocolName protocol name, must be not null
     * @return metric name for the given protocol name, can't be null
     */
    static String protocolsCreatedCountMetric(String protocolName) {
        return PROTOCOLS_CREATED_COUNT.replace("{protocol}", protocolName);
    }

}
