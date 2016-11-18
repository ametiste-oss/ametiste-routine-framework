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
        return notNull(taskSchemasByName.get(taskSchemeName), taskSchemeName);
    }

    @Override
    public <T extends ParamsProtocol> OperationScheme<T> findOperationScheme(final Class<? extends OperationScheme<T>> operationSchemeClass) {
        return notNull(opSchemasByClass.get(operationSchemeClass), operationSchemeClass.getSimpleName());
    }

    @Override
    public <T extends ParamsProtocol> TaskScheme<T> findTaskScheme(final Class<? extends TaskScheme<T>> taskSchemeClass) {
        return notNull(taskSchemasByClass.get(taskSchemeClass), taskSchemeClass.getSimpleName());
    }

    @Override
    public <T extends ParamsProtocol> Class<TaskScheme<T>> findTaskSchemeClass(final String schemeName, final Class<T> paramsClass) {
        // TODO: add params valueType check somehow
        return notNull((Class<TaskScheme<T>>) taskSchemasClassesByName.get(schemeName), schemeName);
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
        return notNull(opSchemasByName.get(operationName), operationName);
    }

    @Override
    public void saveScheme(final OperationScheme<?> operationScheme) {
        putUniqueKey(opSchemasByName, operationScheme.schemeName(), operationScheme);
//        putUniqueKey(opSchemasByClass, operationScheme.getClass(), operationScheme);
        opSchemasByClass.put(operationScheme.getClass(), operationScheme);
        putUniqueKey(opSchemasClassesByName, operationScheme.schemeName(), operationScheme.getClass());
    }

    @Override
    public void saveScheme(final TaskScheme<?> taskScheme) {
        putUniqueKey(taskSchemasByName, taskScheme.schemeName(), taskScheme);
//        putUniqueKey(taskSchemasByClass, taskScheme.getClass(), taskScheme);
        taskSchemasByClass.put(taskScheme.getClass(), taskScheme);
        putUniqueKey(taskSchemasClassesByName, taskScheme.schemeName(), taskScheme.getClass());
    }

    private <K, V> void putUniqueKey(final Map<K, V> container, final K key, final V value) {
        if (container.containsKey(key)) {
            throw new IllegalStateException("Container already has the given value as key: " + key);
        } else {
            container.put(key, value);
        }
    }

    private <T> T notNull(T object, String schemeName) {
        if (object == null) {
            // TODO: custom exception
            throw new RuntimeException("Can't find registered scheme for: " +
                    schemeName);
        }
        return object;
    }

}
