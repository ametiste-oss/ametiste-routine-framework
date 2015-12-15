package org.ametiste.routine.mod.backlog.infrastructure;

import org.ametiste.routine.infrastructure.protocol.ProtocolGatewayService;
import org.ametiste.routine.mod.backlog.domain.RenewSchemeExecutor;
import org.ametiste.routine.mod.backlog.domain.RenewScheme;

/**
 *
 * @since
 */
public class DefaultRenewSchemeExecutor implements RenewSchemeExecutor {

    private final BacklogPopulationStrategiesRegistry registry;
    private final ProtocolGatewayService protocolGatewayservice;

    public DefaultRenewSchemeExecutor(BacklogPopulationStrategiesRegistry registry,
                                      ProtocolGatewayService protocolGatewayservice) {
        this.registry = registry;
        this.protocolGatewayservice = protocolGatewayservice;
    }

    @Override
    public void executeRenewScheme(RenewScheme renewScheme) {

        final BacklogPopulationStrategy populationStrategy =
                registry.findPopulationStrategy(renewScheme.populationStrategyName());

        populationStrategy.populate(protocolGatewayservice.createGateway("mod-backlog"));
        // TODO: exceptions
    }

}
