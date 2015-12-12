package org.ametiste.routine.mod.tasklog.mod;

import org.ametiste.routine.sdk.mod.ModGateway;
import org.ametiste.routine.sdk.mod.ModInfoConsumer;

/**
 *
 * @since
 */
public class TaskLogGateway implements ModGateway {

    @Override
    public void provideModInfo(ModInfoConsumer modInfoConsumer) {
        modInfoConsumer.baseInfo("mod-tasklog", "0.0.1-SNAPSHOT");
    }

}
