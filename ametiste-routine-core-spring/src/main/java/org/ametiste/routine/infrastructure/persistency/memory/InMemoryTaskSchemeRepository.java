package org.ametiste.routine.infrastructure.persistency.memory;

import org.ametiste.routine.domain.scheme.OperationScheme;
import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.domain.scheme.TaskSchemeRepository;
import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.*;

/**
 *
 * @since
 */
public class InMemoryTaskSchemeRepository implements TaskSchemeRepository {

    private final Map<String, TaskScheme> schemas;
    private final Map<Class<? extends TaskScheme>, TaskScheme> taskSchemasByClass = new HashMap<>();
    private final Map<Class<? extends OperationScheme>, OperationScheme> opSchemasByClass = new HashMap<>();

    public InMemoryTaskSchemeRepository(Map<String, TaskScheme> taskSchemas, Map<String, OperationScheme> opSchemas) {
        this.schemas = Collections.unmodifiableMap(taskSchemas);
        this.schemas.values().forEach(
            s -> taskSchemasByClass.put(s.getClass(), s)
        );
        opSchemas.values().forEach(
            s -> opSchemasByClass.put(s.getClass(), s)
        );
    }

    @Override
    public TaskScheme findTaskScheme(String taskSchemeName) {

        if (!schemas.containsKey(taskSchemeName)) {
             throw new IllegalArgumentException("Can't find registered scheme with the given name: " + taskSchemeName);
        }

        return schemas.get(taskSchemeName);
    }

    @Override
    public <T extends ParamsProtocol> OperationScheme<T> findOperationScheme(final Class<? extends OperationScheme<T>> operationSchemeClass) {
        return opSchemasByClass.get(operationSchemeClass);
    }

    @Override
    public <T extends ParamsProtocol> TaskScheme<T> findTaskScheme(final Class<? extends TaskScheme<T>> taskSchemeClass) {
        return taskSchemasByClass.get(taskSchemeClass);
    }

    @Override
    public List<String> loadSchemeNames() {
        return new ArrayList<>(schemas.keySet());
    }

}
