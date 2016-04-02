package org.ametiste.routine.mod.shredder.mod;

import org.ametiste.routine.mod.shredder.configuration.ModShredderProperties;
import org.ametiste.routine.sdk.mod.ModGateway;
import org.ametiste.routine.sdk.mod.ModInfoConsumer;

import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @since
 */
public class ModShredder implements ModGateway {

    public static final String MOD_ID = "mod-shredder";
    private final ModShredderProperties modShredderProperties;

    public ModShredder(ModShredderProperties modShredderProperties) {
        this.modShredderProperties = modShredderProperties;
    }

    @Override
    public void provideModInfo(final ModInfoConsumer modInfoConsumer) {

        final HashMap<String, String> properties = new HashMap<>();

        properties.put("stale.states", modShredderProperties.getStaleStates().toString());
        properties.put("stale.threshold.value", Integer.toString(modShredderProperties.getStaleThreshold().getValue()));
        properties.put("stale.threshold.unit", modShredderProperties.getStaleThreshold().getUnit().toString());

        modInfoConsumer.modInfo(ModShredder.MOD_ID, "1.1", properties);
    }

}
