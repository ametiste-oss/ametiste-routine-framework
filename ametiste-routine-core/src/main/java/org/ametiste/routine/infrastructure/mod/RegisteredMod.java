package org.ametiste.routine.infrastructure.mod;

import org.ametiste.routine.sdk.mod.ModGateway;
import org.ametiste.routine.sdk.mod.ModInfoProvider;

import java.util.*;

/**
 *
 * @since
 */
public class RegisteredMod {

    private final String name;
    private final String version;
    private final List<ModInfoProvider> providers;
    private final Map<String, String> attributes;
    private final ModGateway gateway;

    public RegisteredMod(final String name,
                         final String version,
                         final Map<String, String> attributes,
                         final List<ModInfoProvider> providers,
                         final ModGateway gateway) {
        this.name = name;
        this.version = version;
        this.providers = Collections.unmodifiableList(new ArrayList<>(providers));
        this.attributes = Collections.unmodifiableMap(new HashMap<>(attributes));
        this.gateway = gateway;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public List<ModInfoProvider> getProviders() {
        return providers;
    }
}
