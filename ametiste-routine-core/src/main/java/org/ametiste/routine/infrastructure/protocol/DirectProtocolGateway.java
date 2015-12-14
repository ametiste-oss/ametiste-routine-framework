package org.ametiste.routine.infrastructure.protocol;

import org.ametiste.routine.sdk.mod.protocol.*;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @since
 */
public class DirectProtocolGateway implements ProtocolGateway {

    private final Map<Class<? extends Protocol>, ProtocolFactory> protocols;

    private final GatewayContext gc;

    public DirectProtocolGateway(Map<Class<? extends Protocol>, ProtocolFactory> protocols, GatewayContext gc) {
        this.gc = gc;
        this.protocols = protocols;
    }

    @Override
    public <T extends Protocol> T session(Class<T> protocolType) {

        final Protocol protocol = protocols
                .get(protocolType).createProtocol(gc);

        if (!protocolType.isAssignableFrom(protocol.getClass())) {
             throw new IllegalStateException("Gateway has no access to " +
                     "protocol of the given type: " + protocolType.getName());
        }

        try {
            return protocolType.cast(protocol);
        } catch (ClassCastException e) {
            throw new IllegalStateException("Gateway has error during access to " +
                    "protocol of the given type: " + protocolType.getName(), e);
        }

    }

}
