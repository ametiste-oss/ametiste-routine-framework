package org.ametiste.routine.mod.backlog.configuration;

import org.ametiste.laplatform.sdk.protocol.GatewayContext;
import org.ametiste.laplatform.sdk.protocol.ProtocolFactory;
import org.ametiste.routine.mod.backlog.application.operation.BacklogParams;
import org.ametiste.routine.mod.backlog.application.operation.DirectBacklogParams;
import org.ametiste.routine.mod.backlog.domain.BacklogRepository;
import org.ametiste.routine.mod.backlog.infrastructure.BacklogPopulationStrategiesRegistry;
import org.ametiste.routine.mod.backlog.mod.ModBacklog;
import org.ametiste.routine.mod.backlog.protocol.BacklogProtocol;
import org.ametiste.routine.mod.backlog.protocol.DirectBacklogConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 *
 * @since
 */
@Configuration
@ConditionalOnBean(ModBacklog.class)
public class BacklogProtocolsConfiguration {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private BacklogPopulationStrategiesRegistry strategiesRegistry;

    @Bean
    @Scope(scopeName = "prototype")
    public BacklogParams backlogParamsProtocol(GatewayContext c) {
        return new DirectBacklogParams(c.lookupMap("params"));
    }

    @Bean
    public ProtocolFactory<BacklogParams> backlogParamsProtocolConnectionFactory() {
        return c -> backlogParamsProtocol(c);
    }

    @Bean
    @Scope(scopeName = "prototype")
    public DirectBacklogConnection backlogProtocol(GatewayContext c) {
        return new DirectBacklogConnection(backlogRepository, strategiesRegistry);
    }

    @Bean
    public ProtocolFactory<BacklogProtocol> backlogConnectionFactory() {
        return c -> backlogProtocol(c);
    }

}
