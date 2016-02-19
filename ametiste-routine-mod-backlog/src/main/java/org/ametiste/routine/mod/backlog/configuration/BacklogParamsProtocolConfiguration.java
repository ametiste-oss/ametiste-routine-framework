package org.ametiste.routine.mod.backlog.configuration;

import org.ametiste.laplatform.protocol.GatewayContext;
import org.ametiste.laplatform.protocol.ProtocolFactory;
import org.ametiste.routine.mod.backlog.application.operation.BacklogParams;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 *
 * @since
 */
@Configuration
public class BacklogParamsProtocolConfiguration {

    @Bean
    @Scope(scopeName = "prototype")
    public BacklogParams backlogParamsProtocol(GatewayContext c) {
        return new BacklogParams(
                c.lookupMap("params")
        );
    }

    @Bean
    public ProtocolFactory<BacklogParams> backlogParamsProtocolConnectionFactory() {
        return c -> backlogParamsProtocol(c);
    }

}
