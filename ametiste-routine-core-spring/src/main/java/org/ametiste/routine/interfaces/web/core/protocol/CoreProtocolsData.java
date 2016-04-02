package org.ametiste.routine.interfaces.web.core.protocol;

import java.util.List;

/**
 *
 * @since
 */
public class CoreProtocolsData {

    private final List<ProtocolData> protocols;

    public CoreProtocolsData(List<ProtocolData> protocols) {
        this.protocols = protocols;
    }

    public List<ProtocolData> getProtocols() {
        return protocols;
    }

}
