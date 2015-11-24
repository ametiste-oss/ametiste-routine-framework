package org.ametiste.routine.domain.log;

import java.util.List;
import java.util.UUID;

/**
 *
 * @since
 */
public class OperationLog {

    private final UUID operationId;
    private final String line;
    private final List<NoticeEntry> notices;
    private final String state;

    public OperationLog(UUID operationId, String line, String state, List<NoticeEntry> notices) {
        this.operationId = operationId;
        this.state = state;
        this.line = line;
        this.notices = notices;
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
