package org.ametiste.routine.dsl.configuration;

import org.ametiste.laplatform.protocol.Protocol;
import org.ametiste.laplatform.protocol.gateway.ProtocolGatewayService;
import org.ametiste.routine.dsl.annotations.LambdaProtocol;
import org.ametiste.routine.sdk.mod.ModGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @since
 */
@Configuration
public class ProtocolDSLConfiguration {

    @Autowired(required = false)
    @LambdaProtocol
    private List<Protocol> lambdaProtocols = Collections.emptyList();

    @Autowired
    private ProtocolGatewayService protocolGatewayService;

    @Autowired
    private ApplicationContext applicationContext;

    // TODO: can I do it with bean post processor?
    @Bean
    public ModGateway testDSLProtocolConfig() {

        final List<? extends Class<? extends Protocol>> protocolClasses = lambdaProtocols.stream()
                .map(Protocol::getClass)
                .collect(Collectors.toList());

        protocolClasses.stream()
                .map(this::protoGatewayEntry)
                .forEach(protocolGatewayService::registerGatewayFactory);

        return gw -> {
            // TODO: how can I propagate artifact version?
            gw.modInfo("dsl-protocol", "1.1",
                protocolClasses.stream().collect(Collectors.toMap(s -> s.getName(), s -> ""))
            );
        };
    }

    private ProtocolGatewayService.Entry protoGatewayEntry(Class<? extends Protocol> protocolClass) {

        if (!protocolClass.isAnnotationPresent(LambdaProtocol.class)) {
            throw new IllegalArgumentException("Only @LambdaProtocol classes are allowed.");
        }

        return new ProtocolGatewayService.Entry((Class<? extends Protocol>)protocolClass.getInterfaces()[0], c -> {
            try {
                final Protocol protocol = (Protocol) protocolClass.newInstance();
                applicationContext.getAutowireCapableBeanFactory().autowireBean(protocol);
                return protocol;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

    }

}
