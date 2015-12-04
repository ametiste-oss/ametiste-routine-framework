package org.ametiste.routine.mod.backlog.infrastructure;

/**
 *
 * @since
 */
public interface BacklogPopulationStrategiesRegistry {

    BacklogPopulationStrategy findPopulationStrategy(String strategyName);

}
