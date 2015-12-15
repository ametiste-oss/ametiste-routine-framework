package org.ametiste.routine.infrastructure.protocol;

import org.ametiste.routine.sdk.mod.protocol.Protocol;
import org.ametiste.routine.sdk.mod.protocol.ProtocolFactory;
import org.ametiste.routine.sdk.mod.protocol.ProtocolGateway;

import java.util.Map;

/**
 *
 * @since
 */
public class ProtocolGatewayservice {

    private final Map<Class<? extends Protocol>, ProtocolFactory<?>> protocolFactories;

    public ProtocolGatewayservice(Map<Class<? extends Protocol>, ProtocolFactory<?>> protocolFactories) {
        this.protocolFactories = protocolFactories;
    }

    public ProtocolGateway createGateway(String clientId) {
        // NOTE: PoC implementation, just remap factories
        // in real implementation unique map of protocols will be created for each gateway client call
        // I need to design a ProtocolFamily abstraction for it, gateway will receive family instead of map
        return new DirectProtocolGateway(
            protocolFactories, new DirectGatewayContext(clientId)
        );
    }

}
