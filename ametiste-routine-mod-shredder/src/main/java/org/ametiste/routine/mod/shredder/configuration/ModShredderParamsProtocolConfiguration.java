package org.ametiste.routine.mod.shredder.configuration;

import org.ametiste.laplatform.protocol.GatewayContext;
import org.ametiste.laplatform.protocol.ProtocolFactory;
import org.ametiste.routine.mod.shredder.application.operation.ShreddingParams;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 *
 * @since
 */
@Configuration
public class ModShredderParamsProtocolConfiguration {

    @Bean
    @Scope(scopeName = "prototype")
    public ShreddingParams shreddingParamsProtocol(GatewayContext c) {
        return ShreddingParams.createFromMap(c.lookupMap("params"));
    }

    @Bean
    public ProtocolFactory<ShreddingParams> shreddingParamsProtocolConnectionFactory() {
        return c -> shreddingParamsProtocol(c);
    }

}
