package org.ametiste.routine.sdk.mod.protocol;

import java.util.Map;
import java.util.function.Function;

/**
 *
 * @since
 */
public class GatewayMappers {

    public static GatewayResponseMapper<Integer> asInt(String key) {
        return m -> Integer.valueOf(m.get(key));
    }

    public static GatewayResponseMapper<String> asString(String key) {
        return m -> m.get(key);
    }

}
