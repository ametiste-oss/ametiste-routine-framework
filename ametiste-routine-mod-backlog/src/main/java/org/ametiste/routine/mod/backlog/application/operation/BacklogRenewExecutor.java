package org.ametiste.routine.mod.backlog.application.operation;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.mod.backlog.domain.RenewScheme;
import org.ametiste.routine.mod.backlog.infrastructure.BacklogPopulationStrategy;
import org.ametiste.routine.mod.backlog.protocol.BacklogProtocol;
import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.ametiste.routine.sdk.operation.OperationFeedback;

/**
 *
 * @since
 */
public class BacklogRenewExecutor implements OperationExecutor {

    @Override
    public void execOperation(OperationFeedback feedback, ProtocolGateway protocolGateway) {

        final BacklogProtocol backlogProtocol = protocolGateway.session(BacklogProtocol.class);

        final RenewScheme renewScheme = backlogProtocol.loadRenewSchemeFor(
                protocolGateway.session(BacklogParams.class).schemeName()
        );

        final BacklogPopulationStrategy populationStrategy =
                backlogProtocol.loadPopulationStrategy(renewScheme.populationStrategyName());

        // TODO: add some checks, atm if there is no strategies NPE happens

        populationStrategy.populate(protocolGateway);

    }

}
