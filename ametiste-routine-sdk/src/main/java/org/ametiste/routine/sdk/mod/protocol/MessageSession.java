package org.ametiste.routine.sdk.mod.protocol;

import java.util.Map;

/**
 *
 * @since
 */
public interface MessageSession {

    MessageSession param(String name, String value);

    MessageSession params(Map<String, Map<String, String>> params);

    MessageSession callback(GatewayCallback callback);

    <T> T collect(GatewayResponseMapper<T> collector);

    void end();

}
