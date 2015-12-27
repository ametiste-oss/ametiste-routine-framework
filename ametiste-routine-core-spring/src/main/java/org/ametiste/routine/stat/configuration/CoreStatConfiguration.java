package org.ametiste.routine.stat.configuration;

import org.ametiste.routine.stat.CoreStatCollector;
import org.ametiste.routine.stat.messaging.CoreStatCollectorListener;
import org.ametiste.routine.stat.persistency.InMemoryCoreStatRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @since
 */
@Configuration
public class CoreStatConfiguration {

    @Bean
    public InMemoryCoreStatRepository inMemoryCoreStatRepository() {
        return new InMemoryCoreStatRepository();
    }

    @Bean
    public CoreStatCollector coreStatCollector() {
        return new CoreStatCollector(inMemoryCoreStatRepository());
    }

    @Bean
    public CoreStatCollectorListener coreStatCollectorListener() {
        return new CoreStatCollectorListener(coreStatCollector());
    }

}
