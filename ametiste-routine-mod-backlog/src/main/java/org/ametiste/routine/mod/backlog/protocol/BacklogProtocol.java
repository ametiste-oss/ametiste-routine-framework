package org.ametiste.routine.mod.backlog.protocol;

import org.ametiste.laplatform.protocol.Protocol;
import org.ametiste.routine.mod.backlog.domain.Backlog;
import org.ametiste.routine.mod.backlog.domain.RenewScheme;
import org.ametiste.routine.mod.backlog.infrastructure.BacklogPopulationStrategy;

/**
 *
 * @since
 */
public interface BacklogProtocol extends Protocol {

    RenewScheme loadRenewSchemeFor(String schemeName);

    BacklogPopulationStrategy loadPopulationStrategy(String strategyName);

}