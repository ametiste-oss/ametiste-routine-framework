package org.ametiste.routine.infrastructure.protocol;

import org.ametiste.routine.sdk.mod.protocol.MessageSession;
import org.ametiste.routine.sdk.mod.protocol.Protocol;
import org.ametiste.routine.sdk.mod.protocol.ProtocolSession;

/**
 *
 * @since
 */
public class DirectProtocolSession implements ProtocolSession {

    private final Protocol protocol;

    public DirectProtocolSession(Protocol protocol) {
        this.protocol = protocol;
    }

    @Override
    public MessageSession message(String messageType) {
        return protocol.message(messageType);
    }

}
