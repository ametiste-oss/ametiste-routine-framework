package org.ametiste.routine.domain;

import java.util.Optional;

/**
 *
 * @since
 */
public interface ModRepository {

    void saveModProperty(String modId, String name, String value);

    Optional<String> loadModProperty(String modId, String name);

}
