package org.ametiste.routine.stat.application;

import org.ametiste.metrics.MetricsService;
import org.springframework.core.convert.converter.ConditionalConverter;

/**
 * A metrics source provides a generic metrics set using the given {@link MetricsService}.
 *
 * <p>Implementations of this interface are thread-safe and can be shared. </p>
 *
 * @since 1.1
 */
public interface MetricsSource {

    void provideMetric(MetricsService metricsService);

}
