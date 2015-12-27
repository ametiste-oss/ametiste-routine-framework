package org.ametiste.routine.application.events;

/**
 *
 * @since
 */
public class TasksRemovedEvent {

    private final long taskCountToRemove;
    private final String clientId;

    public TasksRemovedEvent(final long taskCountToRemove, final String clientId) {
        this.taskCountToRemove = taskCountToRemove;
        this.clientId = clientId;
    }

    public long getTaskCountToRemove() {
        return taskCountToRemove;
    }

    public String getClientId() {
        return clientId;
    }
}
