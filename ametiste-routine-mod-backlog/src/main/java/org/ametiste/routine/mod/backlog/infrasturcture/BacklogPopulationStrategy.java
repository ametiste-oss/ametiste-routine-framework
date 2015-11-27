package org.ametiste.routine.mod.backlog.infrasturcture;

import org.ametiste.routine.sdk.mod.DataGateway;
import org.ametiste.routine.sdk.mod.TaskGateway;

/**
 *
 * @since
 */
public interface BacklogPopulationStrategy {

    void populate(TaskGateway taskGateway, DataGateway dataGateway);

}
