package org.ametiste.routine.configuration;

import org.ametiste.laplatform.protocol.GatewayContext;
import org.ametiste.laplatform.protocol.ProtocolFactory;
import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.application.service.termination.TaskTerminationService;
import org.ametiste.routine.domain.ModRepository;
import org.ametiste.routine.infrastructure.protocol.moddata.DirectModDataConnection;
import org.ametiste.routine.infrastructure.protocol.taskcontrol.DirectTaskControlConnection;
import org.ametiste.routine.infrastructure.protocol.taskpool.DirectTaskPoolConnection;
import org.ametiste.routine.sdk.protocol.moddata.ModDataProtocol;
import org.ametiste.routine.sdk.protocol.taskcontrol.TaskControlProtocol;
import org.ametiste.routine.sdk.protocol.taskpool.TaskPoolProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 *
 * @since
 */
@Configuration
public class LPCoreProtocolsConfiguration {

    @Autowired
    private TaskIssueService taskIssueService;

    @Autowired
    private ModRepository modRepository;

    @Autowired
    private TaskTerminationService taskTerminationService;

    @Bean
    @Scope(scopeName = "prototype")
    public TaskPoolProtocol taskPoolProtocol(GatewayContext c) {
        return new DirectTaskPoolConnection(
                c.lookupAttribute("clientId"),
                taskIssueService
        );
    }

    @Bean
    @Scope(scopeName = "prototype")
    public TaskControlProtocol taskControlProtocol(GatewayContext c) {
        return new DirectTaskControlConnection(
                c.lookupAttribute("clientId"),
                taskTerminationService
        );
    }

    @Bean
    @Scope(scopeName = "prototype")
    public ModDataProtocol modDataProtocol(GatewayContext c) {
        return new DirectModDataConnection(
                c.lookupAttribute("clientId"),
                modRepository
        );
    }

    @Bean
    public ProtocolFactory<TaskPoolProtocol> taskPoolProtocolConnectionFactory() {
        return c -> taskPoolProtocol(c);
    }

    @Bean
    public ProtocolFactory<ModDataProtocol> modDataProtocolConnectionFactory() {
        return c -> modDataProtocol(c);
    }

    @Bean
    public ProtocolFactory<TaskControlProtocol> taskControlProtocolConnectionFactory() {
        return c -> taskControlProtocol(c);
    }

}
