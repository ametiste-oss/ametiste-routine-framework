package org.ametiste.routine.mod.backlog.infrasturcture;

import org.ametiste.routine.mod.backlog.domain.RenewSchemeExecutor;
import org.ametiste.routine.mod.backlog.domain.RenewScheme;
import org.ametiste.routine.sdk.mod.DataGateway;
import org.ametiste.routine.sdk.mod.TaskGateway;

/**
 *
 * @since
 */
public class DefaultRenewSchemeExecutor implements RenewSchemeExecutor {

    private final BacklogPopulationStrategiesRegistry registry;
    private final TaskGateway taskGateway;
    private final DataGateway dataGateway;

    public DefaultRenewSchemeExecutor(BacklogPopulationStrategiesRegistry registry,
                                      TaskGateway taskGateway,
                                      DataGateway dataGateway) {
        this.registry = registry;
        this.taskGateway = taskGateway;
        this.dataGateway = dataGateway;
    }

    @Override
    public void executeRenewScheme(RenewScheme renewScheme) {

        final BacklogPopulationStrategy populationStrategy =
                registry.findPopulationStrategy(renewScheme.populationStrategyName());

        populationStrategy.populate(taskGateway, dataGateway);
        // TODO: exceptions
    }

}
