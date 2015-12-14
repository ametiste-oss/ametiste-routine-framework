package org.ametiste.routine.sdk.mod;

import java.util.Optional;

/**
 *
 * @since
 */
@Deprecated
public interface ModDataGateway {

    void storeModData(String name, String value);

    void storeModData(String name, Integer value);

    void storeModData(String name, Long value);

    void storeModData(String name, Boolean value);

    Optional<String> loadModData(String name);

    Optional<Integer> loadModDataInt(String name);

    Optional<Long> loadModDataLong(String name);

    Optional<Boolean> loadModDataBool(String name);

}
