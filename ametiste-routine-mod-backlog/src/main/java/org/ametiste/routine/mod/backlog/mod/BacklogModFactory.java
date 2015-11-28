package org.ametiste.routine.mod.backlog.mod;

import org.ametiste.routine.sdk.mod.DataGateway;
import org.ametiste.routine.sdk.mod.Mod;
import org.ametiste.routine.sdk.mod.ModFactory;
import org.ametiste.routine.sdk.mod.TaskGateway;

/**
 *
 * @since
 */
public class BacklogModFactory implements ModFactory {

    @Override
    public Mod createMod(TaskGateway taskGateway, DataGateway dataGateway) {
        return new BacklogMod();
    }

}
