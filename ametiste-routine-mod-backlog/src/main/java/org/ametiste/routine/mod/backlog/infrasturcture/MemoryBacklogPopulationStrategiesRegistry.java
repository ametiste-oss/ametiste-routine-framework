package org.ametiste.routine.mod.backlog.infrasturcture;

import java.util.Map;

/**
 *
 * @since
 */
public class MemoryBacklogPopulationStrategiesRegistry implements BacklogPopulationStrategiesRegistry {

    private final Map<String, BacklogPopulationStrategy> strategies;

    public MemoryBacklogPopulationStrategiesRegistry(Map<String, BacklogPopulationStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public BacklogPopulationStrategy findPopulationStrategy(String strategyName) {
        return strategies.get(strategyName);
    }

}
