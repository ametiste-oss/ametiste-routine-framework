package org.ametiste.routine.configuration;

import org.ametiste.laplatform.sdk.protocol.GatewayContext;
import org.ametiste.laplatform.sdk.protocol.ProtocolFactory;
import org.ametiste.routine.infrastructure.protocol.http.RestTemplateHttpConnection;
import org.ametiste.routine.sdk.protocol.http.HttpProtocol;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 *     Configures lp-protocol implementation of http connection.
 * </p>
 *
 * <p>
 *     Note, this configuration is disbled by default, to enable this http protocol implementaion
 *     set property <i>{@value AmetisteRoutineCoreProperties#PREFIX}.protocol.http.enabled=true</i>
 * </p>
 *
 * @since 0.1.0
 */
@Configuration
@ConditionalOnProperty(prefix = AmetisteRoutineCoreProperties.PREFIX,
        name = "protocol.http.enabled", matchIfMissing = false)
public class LPHttpProtocolConfiguration {

    @Bean
    @Scope(scopeName = "prototype")
    public HttpProtocol httpProtocol(GatewayContext c) {
        return new RestTemplateHttpConnection(httProtocolRestTemplate());
    }

    @Bean
    public ProtocolFactory<HttpProtocol> httpProtocolConnectionFactory() {
        return c -> httpProtocol(c);
    }

    /**
     * <p>
     *     Dedicated {@link RestTemplate} instance to be used within lpp protocol connections.
     * </p>
     */
    @Bean
    public RestTemplate httProtocolRestTemplate() {
        return new RestTemplate();
    }

}
