package org.ametiste.routine.infrastructure.persistency.memory;

import org.ametiste.routine.domain.scheme.OperationScheme;
import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.domain.scheme.SchemeRepository;
import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.*;

/**
 *
 * @since
 */
public class InMemorySchemeRepository implements SchemeRepository {

    private final Map<String, TaskScheme> taskSchemasByName = new HashMap<>();
    private final Map<Class<? extends TaskScheme>, TaskScheme> taskSchemasByClass = new HashMap<>();
    private final Map<String, Class<? extends TaskScheme>> taskSchemasClassesByName = new HashMap<>();

    private final Map<String, OperationScheme> opSchemasByName = new HashMap<>();
    private final Map<Class<? extends OperationScheme>, OperationScheme> opSchemasByClass = new HashMap<>();
    private final Map<String, Class<? extends OperationScheme>> opSchemasClassesByName = new HashMap<>();

    public InMemorySchemeRepository(Map<String, TaskScheme> taskSchemas, Map<String, OperationScheme> opSchemas) {
        taskSchemas.values().forEach(this::saveScheme);
        opSchemas.values().forEach(this::saveScheme);
    }

    @Override
    public TaskScheme findTaskScheme(String taskSchemeName) {

        if (!taskSchemasByName.containsKey(taskSchemeName)) {
             throw new IllegalArgumentException("Can't find registered scheme with the given name: " + taskSchemeName);
        }

        return taskSchemasByName.get(taskSchemeName);
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
    public <T extends ParamsProtocol> Class<TaskScheme<T>> findTaskSchemeClass(final String schemeName, final Class<T> paramsClass) {
        // TODO: add params type check somehow
        return (Class<TaskScheme<T>>) taskSchemasClassesByName.get(schemeName);
    }

    @Override
    public List<String> loadTaskSchemeNames() {
        return new ArrayList<>(taskSchemasByName.keySet());
    }

    @Override
    public List<String> loadOperationSchemeNames() {
        return new ArrayList<>(opSchemasByName.keySet());
    }

    @Override
    public OperationScheme findOperationScheme(final String operationName) {
        return opSchemasByName.get(operationName);
    }

    @Override
    public void saveScheme(final OperationScheme<?> operationScheme) {
        opSchemasByName.put(operationScheme.schemeName(), operationScheme);
        opSchemasByClass.put(operationScheme.getClass(), operationScheme);
        opSchemasClassesByName.put(operationScheme.schemeName(), operationScheme.getClass());
    }

    @Override
    public void saveScheme(final TaskScheme<?> taskScheme) {
        taskSchemasByName.put(taskScheme.schemeName(), taskScheme);
        taskSchemasByClass.put(taskScheme.getClass(), taskScheme);
        taskSchemasClassesByName.put(taskScheme.schemeName(), taskScheme.getClass());
    }

}
