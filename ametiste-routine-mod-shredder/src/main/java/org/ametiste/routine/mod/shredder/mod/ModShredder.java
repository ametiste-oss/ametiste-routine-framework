package org.ametiste.routine.mod.shredder.mod;

import org.ametiste.routine.sdk.mod.ModGateway;
import org.ametiste.routine.sdk.mod.ModInfoConsumer;

/**
 *
 * @since
 */
public class ModShredder implements ModGateway {

    public static final String MOD_ID = "mod-shredder";

    @Override
    public void provideModInfo(final ModInfoConsumer modInfoConsumer) {
        modInfoConsumer.baseInfo(ModShredder.MOD_ID, "0.0.1");
    }

}
