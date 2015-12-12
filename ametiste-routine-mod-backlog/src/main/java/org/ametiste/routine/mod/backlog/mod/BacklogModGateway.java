package org.ametiste.routine.mod.backlog.mod;

import org.ametiste.routine.sdk.mod.ModInfoConsumer;
import org.ametiste.routine.sdk.mod.ModGateway;

/**
 *
 * @since
 */
public class BacklogModGateway implements ModGateway {

    @Override
    public void provideModInfo(ModInfoConsumer modInfoConsumer) {
        modInfoConsumer.baseInfo("mod-backlog", "0.0.1-SNAPSHOT");
    }

}
