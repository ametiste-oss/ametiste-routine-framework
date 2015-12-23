package org.ametiste.routine.mod.shredder.configuration;

import org.ametiste.laplatform.protocol.gateway.ProtocolGatewayService;
import org.ametiste.routine.configuration.AmetisteRoutineCoreProperties;
import org.ametiste.routine.mod.shredder.application.action.ShreddingAction;
import org.ametiste.routine.mod.shredder.application.service.ShreddingTaskService;
import org.ametiste.routine.mod.shredder.mod.ModShredder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @since
 */
@Configuration
@ConditionalOnProperty(prefix = AmetisteRoutineCoreProperties.PREFIX,
        name = "mod.shredder.enabled", matchIfMissing = false)
public class ModShredderConfiguration {

    @Value("${org.ametiste.routine.mod.shredder.staleThreshold.value:12}")
    private String staleThresholdValue;

    @Value("${org.ametiste.routine.mod.shredder.staleThreshold.unit:HOURS}")
    private String staleThresholdUnit;

    @Autowired
    private ProtocolGatewayService protocolGatewayService;

    @Bean
    public ShreddingTaskService shreddingTaskService() {
        return new ShreddingTaskService(protocolGatewayService, staleThresholdValue, staleThresholdUnit);
    }

    @Bean
    public ShreddingAction shreddingAction() {
        return new ShreddingAction(shreddingTaskService());
    }

    @Bean
    public ModShredder modShredderGateway() {
        return new ModShredder();
    }

}
