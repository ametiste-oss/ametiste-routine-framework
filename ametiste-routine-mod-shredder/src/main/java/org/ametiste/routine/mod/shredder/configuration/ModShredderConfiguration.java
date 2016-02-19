package org.ametiste.routine.mod.shredder.configuration;

import org.ametiste.laplatform.protocol.GatewayContext;
import org.ametiste.laplatform.protocol.ProtocolFactory;
import org.ametiste.laplatform.protocol.gateway.ProtocolGatewayService;
import org.ametiste.routine.configuration.AmetisteRoutineCoreProperties;
import org.ametiste.routine.mod.shredder.application.action.ShreddingAction;
import org.ametiste.routine.mod.shredder.application.operation.ShreddingParams;
import org.ametiste.routine.mod.shredder.application.service.ShreddingTaskService;
import org.ametiste.routine.mod.shredder.mod.ModShredder;
import org.ametiste.routine.sdk.protocol.taskpool.TaskPoolProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 *
 * @since
 */
@Configuration
@ConditionalOnProperty(prefix = ModShredderConfiguration.PREFIX, name = "enabled", matchIfMissing = false)
@EnableConfigurationProperties(ModShredderProperties.class)
public class ModShredderConfiguration {

    public static final String PREFIX = AmetisteRoutineCoreProperties.PREFIX_MOD + ".shredder";

    @Autowired
    private ModShredderProperties props;

    @Autowired
    private ProtocolGatewayService protocolGatewayService;

    @Bean
    public ShreddingTaskService shreddingTaskService() {
        return new ShreddingTaskService(protocolGatewayService,
            props.getStaleStates(),
            props.getStaleThreshold().getValue(),
            props.getStaleThreshold().getUnit()
        );
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
