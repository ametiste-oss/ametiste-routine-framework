package org.ametiste.routine.mod.backlog.infrastructure;

import org.ametiste.laplatform.protocol.ProtocolGateway;

/**
 *
 * @since
 */
public interface BacklogPopulationStrategy {

    void populate(ProtocolGateway gateway);

}
