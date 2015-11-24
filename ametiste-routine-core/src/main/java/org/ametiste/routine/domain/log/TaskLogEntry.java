package org.ametiste.routine.domain.log;

import java.time.Instant;
import java.util.*;

/**
 *
 * @since
 */
public class TaskLogEntry {

    private final UUID taskId;

    private final Instant creationTime;

    private final Instant executionStartTime;

    private final Instant completionTime;

    private final List<NoticeEntry> notices;

    private final String state;

    private final Collection<OperationLog> lines;

    public TaskLogEntry(UUID taskId, Instant creationTime, Instant executionStartTime, Instant completionTime,
                        List<NoticeEntry> notices, String state, Collection<OperationLog> lines) {
        this.taskId = taskId;
        this.creationTime = creationTime;
        this.executionStartTime = executionStartTime;
        this.completionTime = completionTime;
        this.notices = notices;
        this.state = state;
        this.lines = Collections.unmodifiableCollection(lines);
    }

    public List<NoticeEntry> getNotices() {
        return notices;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public Optional<Instant> getExecutionStartTime() {
        return Optional.ofNullable(executionStartTime);
    }

    public Optional<Instant> getCompletionTime() {
        return Optional.ofNullable(completionTime);
    }

    public UUID getTaskId() {
        return taskId;
    }

    public String getState() {
        return state;
    }

    public Collection<OperationLog> getLines() {
        return lines;
    }

}
