package org.ametiste.routine.configuration;

import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.domain.ModRepository;
import org.ametiste.routine.infrastructure.protocol.ProtocolGatewayservice;
import org.ametiste.routine.infrastructure.protocol.moddata.DirectModDataProtocol;
import org.ametiste.routine.infrastructure.protocol.taskpool.DirectTaskPoolProtocol;
import org.ametiste.routine.sdk.mod.ModDataProtocol;
import org.ametiste.routine.sdk.mod.TaskPoolProtocol;
import org.ametiste.routine.sdk.mod.protocol.ProtocolFactory;
import org.ametiste.routine.sdk.mod.protocol.configuration.ProtocolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 *
 * @since
 */
@Configuration
public class ProtocolGatewayServiceConfiguration {

    @Autowired
    private TaskIssueService taskIssueService;

    @Autowired
    private ModRepository modRepository;

    @Autowired
    private List<ProtocolFactory<?>> protocolFactories;

    @Bean
    public ProtocolGatewayservice protocolGatewayService() {
        return new ProtocolGatewayservice(
            ProtocolUtils.protocolsMapping(protocolFactories)
        );
    }

    @Bean
    public ProtocolFactory<TaskPoolProtocol> taskPoolProtocolFactory() {
        return c -> new DirectTaskPoolProtocol(
                c.lookupAttribute("clientId"),
                taskIssueService
        );
    }

    @Bean
    public ProtocolFactory<ModDataProtocol> modDataProtocolFactory() {
        return c -> new DirectModDataProtocol(
                c.lookupAttribute("clientId"),
                modRepository
        );
    }

}
