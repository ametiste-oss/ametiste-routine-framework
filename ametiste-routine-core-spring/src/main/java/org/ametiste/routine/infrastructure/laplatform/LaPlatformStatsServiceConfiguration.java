package org.ametiste.routine.infrastructure.laplatform;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @since
 */
@Configuration
@ConditionalOnProperty(name = "org.ametiste.laplatform.stats.enabled",
        matchIfMissing = true)
public class LaPlatformStatsServiceConfiguration {

    @Bean
    public LaPlatformStatsService laPlatformStatsService() {
         return new LaPlatformStatsService();
    }

    @Bean
    public ProtocolGatewayStatsTool protocolGatewayStatsTool() {
         return new ProtocolGatewayStatsTool(laPlatformStatsService());
    }

}
