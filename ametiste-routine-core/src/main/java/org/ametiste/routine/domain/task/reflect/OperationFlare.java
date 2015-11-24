package org.ametiste.routine.domain.task.reflect;

import org.ametiste.routine.domain.task.notices.Notice;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @since
 */
public class OperationFlare implements Serializable {

    private final UUID id;
    private final String operationLabel;
    private final Map<String, String> properties;
    private final String state;
    private final List<Notice> notices;

    public OperationFlare(UUID id, String operationLabel, Map<String, String> properties, String state, List<Notice> notices) {
        this.id = id;
        this.operationLabel = operationLabel;
        this.properties = properties;
        this.state = state;
        this.notices = notices;
    }

    public UUID flashId() {
        return id;
    }

    public String flashLabel() {
        return operationLabel;
    }

    public String flashState() {
        return state;
    }

    public List<Notice> flashNotices() {
        return notices;
    }

    public Map<String, String> flashProperties() {
        return properties;
    }

}
