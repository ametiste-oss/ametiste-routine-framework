package org.ametiste.routine.domain.scheme;

import org.ametiste.routine.domain.task.ExecutionLine;
import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.List;

import static javafx.scene.input.KeyCode.T;

/**
 *
 * @since
 */
public interface SchemeRepository {

    //TODO: mb I need special representation for "raw" scheme that only has name and fromMap toMap methods
    // for paramteres mapping?
    TaskScheme findTaskScheme(String taskSchemeName);

    <T extends ParamsProtocol> OperationScheme<T>
        findOperationScheme(Class<? extends OperationScheme<T>> operationScheme);

    <T extends ParamsProtocol> TaskScheme<T>
        findTaskScheme(Class<? extends TaskScheme<T>> taskScheme);

    <T extends ParamsProtocol> Class<TaskScheme<T>> findTaskSchemeClass(String schemeName, Class<T> paramsClass);

    List<String> loadTaskSchemeNames();

    List<String> loadOperationSchemeNames();

    OperationScheme findOperationScheme(String operationName);

    void saveScheme(OperationScheme<?> operationScheme);

    void saveScheme(TaskScheme<?> taskScheme);

}
