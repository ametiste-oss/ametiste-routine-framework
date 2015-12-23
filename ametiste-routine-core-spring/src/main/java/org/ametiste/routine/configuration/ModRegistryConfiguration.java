package org.ametiste.routine.configuration;

import org.ametiste.routine.infrastructure.mod.ModRegistry;
import org.ametiste.routine.sdk.mod.ModGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 *
 * @since
 */
@Configuration
public class ModRegistryConfiguration {

    @Autowired
    private List<ModGateway> modGateways;

    @Bean
    public ModRegistry modRegistry() {
        final ModRegistry modRegistry = new ModRegistry();
        modGateways.forEach(
                modRegistry::addMod
        );
        return modRegistry;
    }

}
