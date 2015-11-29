package org.ametiste.routine.domain.scheme;

import org.ametiste.routine.domain.task.Task;

import java.util.Map;

/**
 *
 * @since
 */
public abstract class AbstractTaskScheme implements TaskScheme {

    @Override
    public final Task createTask(Map<String, String> schemeParams, String creatorIdenifier) throws TaskCreationRejectedBySchemeException {
        final Task task = new Task();

        verifyCreationRequest(schemeParams, creatorIdenifier);
        fulfillOperations(task, schemeParams);
        fulfillProperties(task, schemeParams);

        return task;
    }

    protected void verifyCreationRequest(Map<String, String> schemeParams, String creatorIdentifier) throws TaskCreationRejectedBySchemeException {}

    protected void fulfillProperties(Task task, Map<String, String> schemeParams) { }

    protected void fulfillOperations(Task task, Map<String, String> schemeParams) { }

}
