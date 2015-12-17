package org.ametiste.routine.mod.tasklog.domain;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @since
 */
public class OperationLog {

    private final UUID operationId;
    private final String line;
    private final List<NoticeEntry> notices;
    private final Map<String, String> properties;
    private final String state;

    public OperationLog(UUID operationId, String line, String state,
                        List<NoticeEntry> notices, Map<String, String> properties) {
        this.operationId = operationId;
        this.state = state;
        this.line = line;
        this.notices = notices;
        this.properties = properties;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public String getState() {
        return state;
    }

    public UUID getOperationId() {
        return operationId;
    }

    public String getLine() {
        return line;
    }

    public List<NoticeEntry> getNotices() {
        return notices;
    }

}
