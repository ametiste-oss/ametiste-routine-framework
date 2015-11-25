package org.ametiste.routine.domain.task;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @since
 */
public class ExecutionLine implements Serializable {

    private final UUID operationId;
    private final String line;
    private final Map<String, String> properties;

    public ExecutionLine(UUID operationId, String line, Map<String, String> properties) {
        this.operationId = operationId;
        this.line = line;
        this.properties = properties;
    }

    public UUID operationId() {
        return operationId;
    }

    public String line() {
        return line;
    }

    public Map<String, String> properties() {
        return properties;
    }
}
