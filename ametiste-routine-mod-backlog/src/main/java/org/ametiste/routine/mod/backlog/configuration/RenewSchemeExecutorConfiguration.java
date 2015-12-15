package org.ametiste.routine.mod.backlog.configuration;

import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.domain.ModRepository;
import org.ametiste.routine.infrastructure.protocol.ProtocolGatewayservice;
import org.ametiste.routine.mod.backlog.domain.RenewSchemeExecutor;
import org.ametiste.routine.mod.backlog.infrastructure.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 *
 * @since
 */
@Configuration
public class RenewSchemeExecutorConfiguration {

    @Autowired
    private Map<String, BacklogPopulationStrategy> backlogPopulationStrategies;

    @Autowired
    private TaskIssueService taskIssueService;

    @Autowired
    private ModRepository modRepository;

    @Autowired
    private ProtocolGatewayservice protocolGatewayservice;

    @Bean
    public RenewSchemeExecutor renewSchemeExecutor() {
        return new DefaultRenewSchemeExecutor(
                backlogPopulationStrategiesRegistry(),
                protocolGatewayservice
        );
    }

    @Bean
    public BacklogPopulationStrategiesRegistry backlogPopulationStrategiesRegistry() {
        return new MemoryBacklogPopulationStrategiesRegistry(
                backlogPopulationStrategies
        );
    }

}
