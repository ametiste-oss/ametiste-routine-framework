package org.ametiste.routine.mod.backlog.infrastructure;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.sdk.mod.ModDataClient;
import org.ametiste.routine.sdk.mod.ModDataGateway;
import org.ametiste.routine.sdk.mod.TaskGateway;
import org.ametiste.routine.sdk.mod.TaskPoolClient;

/**
 *
 * @since
 */
public interface BacklogPopulationStrategy {

    // TODO: remove default after depricated #populate completle removed
    default void populate(ProtocolGateway gateway) {
        this.populate(new TaskPoolClient(gateway), new ModDataClient(gateway));
    }

    @Deprecated
    default void populate(TaskGateway taskGateway, ModDataGateway modDataGateway) { };

}
