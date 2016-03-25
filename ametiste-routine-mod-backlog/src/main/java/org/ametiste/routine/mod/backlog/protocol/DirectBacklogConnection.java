package org.ametiste.routine.mod.backlog.protocol;

import org.ametiste.routine.mod.backlog.domain.Backlog;
import org.ametiste.routine.mod.backlog.domain.BacklogRepository;
import org.ametiste.routine.mod.backlog.domain.RenewScheme;
import org.ametiste.routine.mod.backlog.domain.RenewSchemeExecutor;
import org.ametiste.routine.mod.backlog.infrastructure.BacklogPopulationStrategiesRegistry;
import org.ametiste.routine.mod.backlog.infrastructure.BacklogPopulationStrategy;

/**
 *
 * @since
 */
public class DirectBacklogConnection implements BacklogProtocol {

    private final BacklogRepository repository;
    private final BacklogPopulationStrategiesRegistry strategiesRegistry;

    public DirectBacklogConnection(BacklogRepository repository, BacklogPopulationStrategiesRegistry strategiesRegistry) {
        this.repository = repository;
        this.strategiesRegistry = strategiesRegistry;
    }

    @Override
    public RenewScheme loadRenewSchemeFor(final String schemeName) {
        final Backlog backlog = repository.loadBacklogOf(schemeName);
        final RenewScheme renewScheme = backlog.createRenewScheme();
        repository.save(backlog);
        return renewScheme;
    }

    @Override
    public BacklogPopulationStrategy loadPopulationStrategy(final String strategyName) {
        return strategiesRegistry.findPopulationStrategy(strategyName);
    }

}
