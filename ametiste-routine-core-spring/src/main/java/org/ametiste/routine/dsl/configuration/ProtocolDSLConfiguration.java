package org.ametiste.routine.dsl.configuration;

import org.ametiste.laplatform.protocol.Protocol;
import org.ametiste.laplatform.protocol.gateway.ProtocolGatewayService;
import org.ametiste.routine.dsl.annotations.LambdaProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 *
 * @since
 */
@Configuration
public class ProtocolDSLConfiguration {

    @Autowired(required = false)
    @LambdaProtocol
    private List<Protocol> lambdaProtocols;

    @Autowired
    private ProtocolGatewayService protocolGatewayService;

    @Autowired
    private ApplicationContext applicationContext;

    // TODO: can I do it with bean post processor?
    @Bean
    public Object testDSLProtocolConfig() {
        if (lambdaProtocols != null) {
            lambdaProtocols.stream()
                    .map(Protocol::getClass)
                    .map(this::protoGatewayEntry)
                    .forEach(protocolGatewayService::registerGatewayFactory);
        }
        return new Object();
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
