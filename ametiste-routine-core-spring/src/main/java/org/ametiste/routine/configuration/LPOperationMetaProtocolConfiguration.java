package org.ametiste.routine.configuration;

import org.ametiste.routine.infrastructure.protocol.operation.OperationMetaProtocolRuntimeFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @since
 */
@Configuration
public class LPOperationMetaProtocolConfiguration {

    @Bean
    public OperationMetaProtocolRuntimeFactory operationMetaProtocolRuntimeFactory() {
        return new OperationMetaProtocolRuntimeFactory();
    }

}
