package org.ametiste.routine.sdk.mod;

/**
 *
 * Exposes general information about registered mod.
 *
 * @since 1.0
 */
public interface ModGateway {

    void provideModInfo(ModInfoConsumer modInfoConsumer);

}
