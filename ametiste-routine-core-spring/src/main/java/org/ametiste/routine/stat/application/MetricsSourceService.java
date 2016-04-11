package org.ametiste.routine.stat.application;

/**
 * Simple metrics source service interface that abstracts the execution of
 * a {@link MetricsSource} set.
 *
 * <p>Implementations can use all sorts of different execution strategies,
 * such as: synchronous, asynchronous, schedulled, using a thread pool, and more.
 *
 * @see MetricsSource
 * @since 1.1
 */
// TODO: would be nice to see it as part of ame-metrics project
public interface MetricsSourceService {

    void provideMetrics();

}
