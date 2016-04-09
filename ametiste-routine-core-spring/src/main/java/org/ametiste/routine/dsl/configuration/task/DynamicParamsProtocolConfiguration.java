package org.ametiste.routine.dsl.configuration.task;

import org.ametiste.laplatform.sdk.protocol.GatewayContext;
import org.ametiste.laplatform.sdk.protocol.ProtocolFactory;
import org.ametiste.routine.dsl.application.DirectDynamicParamsProtocol;
import org.ametiste.routine.dsl.application.DynamicParamsProtocol;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 *
 * @since 1.1
 */
@Configuration
public class DynamicParamsProtocolConfiguration {

    @Bean
    @Scope(scopeName = "prototype")
    public DynamicParamsProtocol dynamicParamsProtocol(GatewayContext context) {
        return new DirectDynamicParamsProtocol(context.lookupMap("params"));
    }

    @Bean
    public ProtocolFactory<DynamicParamsProtocol> dynamicParamsProtocolConnection() {
        return c -> dynamicParamsProtocol(c);
    }

}
