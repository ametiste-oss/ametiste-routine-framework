package org.ametiste.routine.configuration.protocol;

import org.ametiste.routine.infrastructure.protocol.operation.OperationMetaProtocolRuntime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @since
 */
@Configuration
public class LPOperationMetaProtocolConfiguration {

    @Bean
    public OperationMetaProtocolRuntime operationMetaProtocolRuntimeFactory() {
        return new OperationMetaProtocolRuntime();
    }

}
