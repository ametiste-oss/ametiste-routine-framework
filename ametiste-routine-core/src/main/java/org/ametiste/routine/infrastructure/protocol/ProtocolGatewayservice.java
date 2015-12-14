package org.ametiste.routine.infrastructure.protocol;

import org.ametiste.routine.sdk.mod.protocol.ProtocolFactory;
import org.ametiste.routine.sdk.mod.protocol.ProtocolFamily;
import org.ametiste.routine.sdk.mod.protocol.ProtocolGateway;

import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @since
 */
public class ProtocolGatewayService {

    private final Map<String, ProtocolFactory> protocolFactories;

    public ProtocolGatewayService(Map<String, ProtocolFactory> protocolFactories) {
        this.protocolFactories = protocolFactories;
    }

    public ProtocolGateway createGateway() {
        // NOTE: PoC implementation, just remap factories
        // in real implementation unique map of protocols will be created for each gateway client call
        return new DirectProtocolGateway(
            protocolFactories.entrySet()
                    .stream()
                    .collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue().createProtocol()))
        );
    }

}
