package org.ametiste.routine.sdk.mod.protocol;

import java.util.Map;

/**
 *
 * @since
 */
public interface GatewayResponseMapper<T> {

    T map(Map<String, String> response);

}
