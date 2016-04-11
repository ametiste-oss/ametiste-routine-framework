package org.ametiste.routine.stat.interfaces.metrics;

import org.ametiste.metrics.MetricsService;
import org.ametiste.routine.infrastructure.laplatform.LaPlatformStatsService;
import org.ametiste.routine.stat.application.MetricsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static org.ametiste.routine.stat.interfaces.metrics.LaPlatformMetrics.protocolsCreatedCountMetric;

/**
 * This {@link MetricsSource} provides {@link LaPlatformStatsService} data as {@code gauge} metrics
 * <ul>
 *     <li>{@value LaPlatformMetrics#PROTOCOLS_CREATED_COUNT}</li>
 * </ul>
 * <p>
 * Metrics are provided only for the current application runtime session.
 * <p>
 * This source will be active only in a context, where {@link LaPlatformStatsService}
 * bean is present.
 * <p>
 * This source may be deactivated by property<br>
 * <br>
 * <i>org.ametiste.routine.metrics.source.platform.protocols.enabled=false</i>
 *
 * @see LaPlatformMetrics
 * @see LaPlatformStatsService
 * @since 1.1
 */
@Component
@ConditionalOnProperty(name = "org.ametiste.routine.metrics.source.platform.protocols.enabled", matchIfMissing = true)
@ConditionalOnBean(LaPlatformStatsService.class)
public class LaPlatformStatsMetricsSource implements MetricsSource {

    @Autowired
    private LaPlatformStatsService laPlatformStatsService;

    @Override
    public void provideMetric(final MetricsService metricsService) {
        laPlatformStatsService.loadProtocolStats(
                s -> metricsService.gauge(protocolsCreatedCountMetric(s.name()), (int) s.createdForPeriod())
        );
    }

}
