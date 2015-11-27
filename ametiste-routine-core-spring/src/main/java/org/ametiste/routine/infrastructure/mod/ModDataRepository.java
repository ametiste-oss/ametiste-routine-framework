package org.ametiste.routine.infrastructure.mod;

import java.util.Optional;

/**
 *
 * @since
 */
public interface ModDataRepository {

    void saveModProperty(String modId, String name, String value);

    Optional<String> loadModProperty(String modId, String name);

}
