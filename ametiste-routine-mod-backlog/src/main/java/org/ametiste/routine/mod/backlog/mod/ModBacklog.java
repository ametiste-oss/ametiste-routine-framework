package org.ametiste.routine.mod.backlog.mod;

import org.ametiste.routine.sdk.mod.ModInfoConsumer;
import org.ametiste.routine.sdk.mod.ModGateway;

/**
 *
 * @since
 */
public class ModBacklog implements ModGateway {

    public static final String MOD_ID = "mod-backlog";

    @Override
    public void provideModInfo(ModInfoConsumer modInfoConsumer) {
        modInfoConsumer.baseInfo(MOD_ID, "0.0.1-SNAPSHOT");
    }

}
