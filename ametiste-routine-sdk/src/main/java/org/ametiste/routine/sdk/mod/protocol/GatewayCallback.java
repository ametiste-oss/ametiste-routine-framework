package org.ametiste.routine.sdk.mod.protocol;

import java.util.Map;

/**
 *
 * @since
 */
@FunctionalInterface
public interface GatewayCallback {

    void call(Map<String, String> value);

}
