package org.ametiste.routine.configuration;

import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.domain.ModRepository;
import org.ametiste.routine.infrastructure.protocol.ModDataProtocol;
import org.ametiste.routine.infrastructure.protocol.ProtocolGatewayService;
import org.ametiste.routine.infrastructure.protocol.taskpool.TaskPoolProtocol;
import org.ametiste.routine.sdk.mod.TaskPool;
import org.ametiste.routine.sdk.mod.protocol.ProtocolFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 *
 * @since
 */
@Configuration
public class ProtocolGatewayServiceConfiguration {

    @Autowired
    private Map<String, ProtocolFactory> protocolFactories;

    @Autowired
    private TaskIssueService taskIssueService;

    @Autowired
    private ModRepository modRepository;

    @Bean
    public ProtocolGatewayService protocolGatewayService() {
         return new ProtocolGatewayService(protocolFactories);
    }

    @Bean(name = TaskPool.PROTOCOL_NAME)
    public ProtocolFactory taskPoolProtocolFactory() {
        return () -> new TaskPoolProtocol(taskIssueService);
    }

    @Bean(name = "mod-data")
    public ProtocolFactory modDataProtocolFactory() {
        return () -> new ModDataProtocol(modRepository);
    }

}
