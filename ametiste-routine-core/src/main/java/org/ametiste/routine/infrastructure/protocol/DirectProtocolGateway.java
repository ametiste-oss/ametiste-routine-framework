package org.ametiste.routine.infrastructure.protocol;

import org.ametiste.routine.sdk.mod.protocol.*;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @since
 */
public class DirectProtocolGateway implements ProtocolGateway {

    private final Map<String, Protocol> protocols;

    public DirectProtocolGateway(Map<String, Protocol> protocols) {
        this.protocols = protocols;
    }

    @Override
    public ProtocolSession session(String protocolType) {
        final Protocol protocol = protocols.get(protocolType);
        return new DirectProtocolSession(protocol);
    }

}
