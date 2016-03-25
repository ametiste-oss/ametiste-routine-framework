package org.ametiste.routine.domain.scheme;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.Map;
import java.util.function.Consumer;

/**
 *
 * @since
 */
public class TaskBuilder<T extends ParamsProtocol> {

    private final TaskSchemeRepository taskSchemeRepository;
    private final String creatorIdentifier;

    private Task task;
    private TaskScheme<T> taskScheme;

    public TaskBuilder(TaskSchemeRepository taskSchemeRepository, String creatorIdentifier) {
        this.task = new Task();
        this.taskSchemeRepository = taskSchemeRepository;
        this.creatorIdentifier = creatorIdentifier;
    }

    public TaskBuilder<T> defineScheme(Class<? extends TaskScheme<T>> taskSchemeClass, Consumer<T> schemeParamsInstaller) {

        taskScheme = taskSchemeRepository.findTaskScheme(taskSchemeClass);
        task = new Task();

        try {
            taskScheme.setupTask(this, schemeParamsInstaller, creatorIdentifier);
        } catch (TaskSchemeException e) {
            // TODO: add specific exception
            throw new RuntimeException("Task creation error", e);
        }

        return this;
    }

    public <S extends ParamsProtocol> TaskBuilder<T> addOperation(final Class<? extends OperationScheme<S>> operationScheme,
                                                                  final Consumer<S> paramsInstaller) {
        final OperationScheme<S> scheme =
                taskSchemeRepository.findOperationScheme(operationScheme);
        scheme.createOperationFor(this::addOperation, paramsInstaller);
        return this;
    }

    public TaskBuilder<T> addOperation(String label, ParamsProtocol params) {
        task.addOperation(label, params.asMap());
        return this;
    }

    public TaskBuilder<T> addProperty(final String propName, final String propValue) {
        task.addProperty(new TaskProperty(propName, propValue));
        return this;
    }

    public Task build() {
        return task;
    }

}
