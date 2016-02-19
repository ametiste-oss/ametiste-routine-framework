package org.ametiste.routine.mod.backlog.infrastructure;

import org.ametiste.laplatform.protocol.gateway.ProtocolGatewayService;
import org.ametiste.routine.mod.backlog.domain.RenewScheme;
import org.ametiste.routine.mod.backlog.domain.RenewSchemeExecutor;
import org.ametiste.routine.mod.backlog.mod.ModBacklog;

import java.util.Collections;

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

        populationStrategy.populate(
                protocolGatewayservice.createGateway(ModBacklog.MOD_ID, Collections.emptyMap())
        );
        // TODO: exceptions
    }

}
