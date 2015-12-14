package org.ametiste.routine.sdk.mod.protocol;

import java.util.Map;

/**
 *
 * @since
 */
public interface MessageSession {

    void params(Map<String, Map<String, String>> params);

    void callback(GatewayCallback callback);

    <T> T collect(GatewayResponseMapper<T> collector);

    void end();

}
