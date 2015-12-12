package org.ametiste.routine.infrastructure.mod;

import org.ametiste.routine.sdk.mod.ModGateway;

/**
 *
 * @since
 */
public class RegisteredMod {

    private final String name;
    private final String version;
    private final ModGateway gateway;

    public RegisteredMod(String name, String version, ModGateway gateway) {
        this.name = name;
        this.version = version;
        this.gateway = gateway;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

}
