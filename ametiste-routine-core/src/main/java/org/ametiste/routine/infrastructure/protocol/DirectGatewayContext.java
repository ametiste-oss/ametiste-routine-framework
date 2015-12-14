package org.ametiste.routine.infrastructure.protocol;

import org.ametiste.routine.sdk.mod.protocol.GatewayContext;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @since
 */
public class DirectGatewayContext implements GatewayContext {

    private final Map<String, String> attributes = new HashMap<>();

    public DirectGatewayContext(String clientId) {
        attributes.put("clientId", clientId);
    }

    @Override
    public String lookupAttribute(String name) {
        return attributes.get(name);
    }

}
