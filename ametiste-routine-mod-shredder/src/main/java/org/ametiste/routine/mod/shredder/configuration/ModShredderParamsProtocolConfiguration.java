package org.ametiste.routine.mod.shredder.configuration;

import org.ametiste.laplatform.sdk.protocol.GatewayContext;
import org.ametiste.laplatform.sdk.protocol.ProtocolFactory;
import org.ametiste.routine.mod.shredder.application.operation.DirectShreddingParams;
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
        return DirectShreddingParams.createFromMap(c.lookupMap("params"));
    }

    @Bean
    public ProtocolFactory<ShreddingParams> shreddingParamsProtocolConnectionFactory() {
        return c -> shreddingParamsProtocol(c);
    }

}
