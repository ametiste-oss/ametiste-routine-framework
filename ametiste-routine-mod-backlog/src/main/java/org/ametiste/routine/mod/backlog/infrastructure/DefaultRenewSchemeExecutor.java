package org.ametiste.routine.mod.backlog.infrastructure;

import org.ametiste.routine.infrastructure.protocol.ProtocolGatewayService;
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
    private final ProtocolGatewayService protocolGatewayService;

    public DefaultRenewSchemeExecutor(BacklogPopulationStrategiesRegistry registry,
                                      ProtocolGatewayService protocolGatewayService) {
        this.registry = registry;
        this.protocolGatewayService = protocolGatewayService;
    }

    @Override
    public void executeRenewScheme(RenewScheme renewScheme) {

        final BacklogPopulationStrategy populationStrategy =
                registry.findPopulationStrategy(renewScheme.populationStrategyName());

        populationStrategy.populate(protocolGatewayService.createGateway("mod-backlog"));
        // TODO: exceptions
    }

}
