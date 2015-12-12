package org.ametiste.routine.infrastructure.mod;

import org.ametiste.routine.sdk.mod.ModGateway;

import java.util.*;

/**
 *
 * @since
 */
public class ModRegistry {

    private Map<String, RegisteredMod> registeredMods;

    public ModRegistry() {
        this.registeredMods = new HashMap<>();
    }

    public void addMod(ModGateway modGateway) {
        modGateway.provideModInfo((n, v) -> {
            registeredMods.put(n, new RegisteredMod(n, v, modGateway));
        });
    }

    public List<RegisteredMod> loadMods() {
        return new ArrayList<>(registeredMods.values());
    }

}
