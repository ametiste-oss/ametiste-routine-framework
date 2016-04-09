package org.ametiste.routine.interfaces.web.core.protocol;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @since
 */
public class CoreProtocolsData {

    private final HashMap<String, List<ProtocolData>> protocolGroups;

    public CoreProtocolsData(final HashMap<String, List<ProtocolData>> protocolGroups) {
        this.protocolGroups = protocolGroups;
    }

    public HashMap<String, List<ProtocolData>> getGroups() {
        return protocolGroups;
    }

}
