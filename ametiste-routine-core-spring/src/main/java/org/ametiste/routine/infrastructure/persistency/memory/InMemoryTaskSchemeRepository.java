package org.ametiste.routine.infrastructure.persistency.memory;

import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.domain.scheme.TaskSchemeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @since
 */
public class InMemoryTaskSchemeRepository implements TaskSchemeRepository {

    private final Map<String, TaskScheme> schemas;

    public InMemoryTaskSchemeRepository(Map<String, TaskScheme> schemas) {
        this.schemas = schemas;
    }

    @Override
    public TaskScheme findTaskScheme(String taskSchemeName) {

        if (!schemas.containsKey(taskSchemeName)) {
             throw new IllegalArgumentException("Can't find registered scheme with the given name: " + taskSchemeName);
        }

        return schemas.get(taskSchemeName);
    }

    @Override
    public List<String> loadSchemeNames() {
        return new ArrayList<>(schemas.keySet());
    }

}
