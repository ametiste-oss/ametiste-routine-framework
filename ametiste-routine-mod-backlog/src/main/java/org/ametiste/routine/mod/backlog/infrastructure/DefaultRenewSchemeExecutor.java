package org.ametiste.routine.mod.backlog.infrastructure;

import org.ametiste.routine.mod.backlog.domain.RenewSchemeExecutor;
import org.ametiste.routine.mod.backlog.domain.RenewScheme;
import org.ametiste.routine.sdk.mod.ModDataGateway;
import org.ametiste.routine.sdk.mod.TaskGateway;

/**
 *
 * @since
 */
public class DefaultRenewSchemeExecutor implements RenewSchemeExecutor {

    private final BacklogPopulationStrategiesRegistry registry;
    private final TaskGateway taskGateway;
    private final ModDataGateway modDataGateway;

    public DefaultRenewSchemeExecutor(BacklogPopulationStrategiesRegistry registry,
                                      TaskGateway taskGateway,
                                      ModDataGateway modDataGateway) {
        this.registry = registry;
        this.taskGateway = taskGateway;
        this.modDataGateway = modDataGateway;
    }

    @Override
    public void executeRenewScheme(RenewScheme renewScheme) {

        final BacklogPopulationStrategy populationStrategy =
                registry.findPopulationStrategy(renewScheme.populationStrategyName());

        populationStrategy.populate(taskGateway, modDataGateway);
        // TODO: exceptions
    }

}
