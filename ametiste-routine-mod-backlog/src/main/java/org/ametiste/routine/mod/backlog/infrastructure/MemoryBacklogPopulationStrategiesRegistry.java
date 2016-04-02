package org.ametiste.routine.mod.backlog.infrastructure;

import org.ametiste.routine.mod.backlog.domain.Backlog;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @since
 */
public class MemoryBacklogPopulationStrategiesRegistry implements BacklogPopulationStrategiesRegistry {

    private final Map<String, BacklogPopulationStrategy> strategies;

    public MemoryBacklogPopulationStrategiesRegistry(Map<String, BacklogPopulationStrategy> strategies) {
        this.strategies = Optional.ofNullable(strategies)
                .map(m -> new HashMap<>(m))
                .orElseGet(HashMap::new);
    }

    @Override
    public BacklogPopulationStrategy findPopulationStrategy(String strategyName) {
        return strategies.get(strategyName);
    }

    @Override
    public void registerPopulationStrategy(final String stragetyName, final BacklogPopulationStrategy backlogPopulationStrategy) {
        strategies.put(stragetyName, backlogPopulationStrategy);
    }

}
