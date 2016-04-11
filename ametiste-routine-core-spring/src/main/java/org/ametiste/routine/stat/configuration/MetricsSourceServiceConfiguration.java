package org.ametiste.routine.stat.configuration;

import org.ametiste.metrics.MetricsService;
import org.ametiste.routine.stat.application.BasicMetricsSourceService;
import org.ametiste.routine.stat.application.MetricsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Collections;
import java.util.List;

/**
 * Configures metrics provider service.
 *
 * <p>
 *     This configuration module collects all {@link MetricsSource} instances registered
 *     within the context and creates {@link BasicMetricsSourceService} for these providers.
 * </p>
 *
 * @since 1.1
 */
@Configuration
public class MetricsSourceServiceConfiguration {

    @Autowired(required = false)
    private List<MetricsSource> metricsSources = Collections.emptyList();

    @Autowired
    private MetricsService metricsService;

    @Bean
    public BasicMetricsSourceService metricsSourceService() {
        return new BasicMetricsSourceService(metricsService, metricsSources);
    }

    @Scheduled(fixedDelay = 30000)
    public void scheduleMetricsProviding() {
        metricsSourceService().provideMetrics();
    }

}
