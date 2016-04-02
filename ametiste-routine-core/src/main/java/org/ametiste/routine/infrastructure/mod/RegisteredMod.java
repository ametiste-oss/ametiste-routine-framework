package org.ametiste.routine.infrastructure.mod;

import org.ametiste.routine.sdk.mod.ModGateway;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @since
 */
public class RegisteredMod {

    private final String name;
    private final String version;
    private final Map<String, String> attributes;
    private final ModGateway gateway;

    public RegisteredMod(String name, String version, Map<String, String> attributes, ModGateway gateway) {
        this.name = name;
        this.version = version;
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

}
