package org.ametiste.routine.configuration;

import org.ametiste.laplatform.protocol.GatewayContext;
import org.ametiste.laplatform.protocol.ProtocolFactory;
import org.ametiste.laplatform.protocol.configuration.ProtocolUtils;
import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.domain.ModRepository;
import org.ametiste.routine.infrastructure.protocol.ProtocolGatewayService;
import org.ametiste.routine.infrastructure.protocol.moddata.DirectModDataProtocol;
import org.ametiste.routine.infrastructure.protocol.taskpool.DirectTaskPoolProtocol;
import org.ametiste.routine.sdk.mod.ModDataProtocol;
import org.ametiste.routine.sdk.mod.TaskPoolProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.List;

/**
 *
 * @since
 */
// TODO: extract to lambdaplatform-protocol-srping-starter
@Configuration
public class ProtocolGatewayServiceConfiguration {

    @Autowired
    private List<ProtocolFactory<?>> protocolFactories;

    @Bean
    public ProtocolGatewayService protocolGatewayService() {
        return new ProtocolGatewayService(
            ProtocolUtils.protocolsMapping(protocolFactories)
        );
    }

}
