package org.ametiste.routine.mod.backlog.infrastructure;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.sdk.protocol.moddata.ModDataClient;
import org.ametiste.routine.sdk.mod.ModDataGateway;
import org.ametiste.routine.sdk.mod.TaskGateway;
import org.ametiste.routine.sdk.protocol.taskpool.TaskPoolClient;

/**
 *
 * @since
 */
public interface BacklogPopulationStrategy {

    void populate(ProtocolGateway gateway);

}
