package org.ametiste.routine.stat.application;

import org.ametiste.metrics.MetricsService;
import org.ametiste.routine.stat.configuration.MetricsSourceServiceConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * Basic implementation of {@link MetricsSourceService} that just
 * pass control to the each of enclosed {@link MetricsSource} one by one.
 *
 * <p> During the exection, this service will ensure that the each enclosed source
 * will take the control, regardless of the others sources unhandeled errors.
 *
 * <p> Note, execution mechanism is relay on runtime platform, this implementation does
 * not provide any execution mechanism, such as thread pool or scheduling. For example,
 * for {@code Spring Framework} runtime it may be {@link Scheduled} configuration method.
 * See {@link MetricsSourceServiceConfiguration} for such configuration details.
 *
 * @see MetricsSource
 * @see MetricsSourceServiceConfiguration
 *
 * @since 1.1
 */
public class BasicMetricsSourceService implements MetricsSourceService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final MetricsService metricsService;
    private final List<MetricsSource> metricsSources;

    public BasicMetricsSourceService(final MetricsService metricsService,
                                     final List<MetricsSource> metricsSources) {
        this.metricsService = metricsService;
        this.metricsSources = metricsSources;
    }

    @Override
    public void provideMetrics() {
        metricsSources.forEach(this::provideSafe);
    }

    private void provideSafe(MetricsSource metricsSource) {
        try {
            metricsSource.provideMetric(metricsService);
        } catch (Throwable e) {
            if (logger.isErrorEnabled()) {
                logger.error("Error during metrics provider execution: " + metricsSource.getClass().getName(), e);
            }
        }
    }

}
