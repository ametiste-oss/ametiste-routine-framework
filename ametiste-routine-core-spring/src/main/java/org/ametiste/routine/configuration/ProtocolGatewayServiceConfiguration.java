package org.ametiste.routine.configuration;

import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.domain.ModRepository;
import org.ametiste.routine.infrastructure.protocol.moddata.DirectModDataProtocol;
import org.ametiste.routine.infrastructure.protocol.ProtocolGatewayService;
import org.ametiste.routine.infrastructure.protocol.taskpool.DirectTaskPoolProtocol;
import org.ametiste.routine.sdk.mod.ModDataProtocol;
import org.ametiste.routine.sdk.mod.TaskPoolProtocol;
import org.ametiste.routine.sdk.mod.protocol.Protocol;
import org.ametiste.routine.sdk.mod.protocol.ProtocolFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Bean
    public ProtocolGatewayService protocolGatewayService() {

        final Map<Class<? extends Protocol>, ProtocolFactory> protocolsMapping = new HashMap<>();
        protocolsMapping.put(TaskPoolProtocol.class, taskPoolProtocolFactory());
        protocolsMapping.put(ModDataProtocol.class, modDataProtocolFactory());

        return new ProtocolGatewayService(protocolsMapping);
    }

    @Bean
    public ProtocolFactory taskPoolProtocolFactory() {
        return (c) -> new DirectTaskPoolProtocol(
                c.lookupAttribute("clientId"),
                taskIssueService
        );
    }

    @Bean
    public ProtocolFactory modDataProtocolFactory() {
        return (c) -> new DirectModDataProtocol(
                c.lookupAttribute("clientId"),
                modRepository
        );
    }

}
