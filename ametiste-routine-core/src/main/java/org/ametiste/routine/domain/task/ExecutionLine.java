package org.ametiste.routine.domain.task;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *   Describes ne operationName from execution order, this operationName defines what shuld be done and
 *   conveys paramters for this execution process.
 * </p>
 *
 * @since 0.-1.0
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

    public String operationName() {
        return line;
    }

    public Map<String, String> properties() {
        return properties;
    }
}
