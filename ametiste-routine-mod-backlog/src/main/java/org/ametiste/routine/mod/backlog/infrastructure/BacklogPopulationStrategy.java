package org.ametiste.routine.mod.backlog.infrastructure;

import org.ametiste.routine.sdk.mod.ModDataClient;
import org.ametiste.routine.sdk.mod.ModDataGateway;
import org.ametiste.routine.sdk.mod.TaskGateway;
import org.ametiste.routine.sdk.mod.TaskPoolClient;
import org.ametiste.routine.sdk.mod.protocol.ProtocolGateway;

/**
 *
 * @since
 */
public interface BacklogPopulationStrategy {

    default void populate(ProtocolGateway gateway) {
        this.populate(new TaskPoolClient(gateway), new ModDataClient(gateway));
    }

    @Deprecated
    void populate(TaskGateway taskGateway, ModDataGateway modDataGateway);

}
