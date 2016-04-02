package org.ametiste.routine.mod.backlog.mod;

import org.ametiste.routine.sdk.mod.ModInfoConsumer;
import org.ametiste.routine.sdk.mod.ModGateway;

import java.util.Collections;

/**
 *
 * @since
 */
public class ModBacklog implements ModGateway {

    public static final String MOD_ID = "mod-backlog";
    private final Long renewRate;

    public ModBacklog(Long renewRate) {
        this.renewRate = renewRate;
    }

    @Override
    public void provideModInfo(ModInfoConsumer modInfoConsumer) {
        modInfoConsumer.modInfo(MOD_ID, "1.1", Collections.singletonMap("renew.rate", renewRate.toString()));
    }

}
