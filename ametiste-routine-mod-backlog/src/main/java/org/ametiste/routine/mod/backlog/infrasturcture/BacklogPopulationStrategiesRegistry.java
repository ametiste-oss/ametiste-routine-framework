package org.ametiste.routine.mod.backlog.infrasturcture;

/**
 *
 * @since
 */
public interface BacklogPopulationStrategiesRegistry {

    BacklogPopulationStrategy findPopulationStrategy(String strategyName);

}
