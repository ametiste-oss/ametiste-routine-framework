package org.ametiste.routine.mod.tasklog.interfaces.web.resources;

import org.springframework.hateoas.core.Relation;

import java.util.List;
import java.util.UUID;

/**
 *
 * @since
 */
// FIXME: Looks like bug in spring-hateoas, but it is using content anotations to resolve resource rels
@Relation(value="sro:task-log-entry", collectionRelation = "sro:task-log-entries")
public class TaskLogEntryContent {

    private UUID taskId;

    private String state;

    private final List<NoticeDTO> notices;

    private final List<OperationLogDTO> operations;

    public TaskLogEntryContent(UUID taskId, String state, List<NoticeDTO> notices, List<OperationLogDTO> operations) {
        this.taskId = taskId;
        this.state = state;
        this.notices = notices;
        this.operations = operations;
    }

    public List<NoticeDTO> getNotices() {
        return notices;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public String getState() {
        return state;
    }

    public List<OperationLogDTO> getOperations() {
        return operations;
    }
}
