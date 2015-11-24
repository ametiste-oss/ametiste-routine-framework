package org.ametiste.routine.domain.scheme;

import java.util.List;

/**
 *
 * @since
 */
public interface TaskSchemeRepository {

    TaskScheme findTaskScheme(String taskSchemeName);

    List<String> loadSchemeNames();

}
