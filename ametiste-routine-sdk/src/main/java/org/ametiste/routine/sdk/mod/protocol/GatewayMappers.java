package org.ametiste.routine.sdk.mod.protocol;

import java.util.Optional;

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

    public static GatewayResponseMapper<Optional<String>> asOptionalString(String key) {
        return m -> Optional.ofNullable(m.get(key));
    }

}
