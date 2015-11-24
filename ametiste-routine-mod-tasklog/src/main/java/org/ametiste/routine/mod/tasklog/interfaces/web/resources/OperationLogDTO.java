package org.ametiste.routine.mod.tasklog.interfaces.web.resources;

import java.util.List;
import java.util.UUID;

/**
 *
 * @since
 */
public class OperationLogDTO {

    private final UUID operationId;

    private final String state;

    private final String description;
    private final List<NoticeDTO> notices;

    public OperationLogDTO(UUID operationId, String state, String description, List<NoticeDTO> notices) {
        this.operationId = operationId;
        this.state = state;
        this.description = description;
        this.notices = notices;
    }

    public UUID getOperationId() {
        return operationId;
    }

    public String getState() {
        return state;
    }

    public String getDescription() {
        return description;
    }

    public List<NoticeDTO> getNotices() {
        return notices;
    }
}
