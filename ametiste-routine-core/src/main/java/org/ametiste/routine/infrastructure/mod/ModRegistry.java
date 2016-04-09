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
        // TODO: some validation required, for example "mod::blabla::id" is invalid name cos we cant collect metrics
        // for this name
        // I guess only [a-z0-9-] should be allowed to be mod identifiers
        modGateway.provideModInfo((name, version, attrs, infoProviders) -> {
            registeredMods.put(name, new RegisteredMod(name, version, attrs, infoProviders, modGateway));
        });
    }

    public List<RegisteredMod> loadMods() {
        return new ArrayList<>(registeredMods.values());
    }

}
