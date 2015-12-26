package org.ametiste.routine.domain;

import java.util.Optional;

/**
 *
 * @since
 */
// TODO: rename to ModDataRepository
public interface ModRepository {

    void saveModProperty(String modId, String name, String value);

    Optional<String> loadModProperty(String modId, String name);

}
