package org.ametiste.routine.domain.task;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 *
 * @since
 */
public class ExecutionOrder implements Serializable {

    private final Collection<ExecutionLine> executionLines;
    private final UUID taskId;

    public ExecutionOrder(UUID taskId, List<ExecutionLine> executionLines) {
        this.taskId = taskId;
        this.executionLines = Collections.unmodifiableCollection(executionLines);
    }

    public UUID boundTaskId() {
        return taskId;
    }

    @Deprecated
    public Collection<ExecutionLine> executionLines() {
        return executionLines;
    }

    public void executionLines(Consumer<ExecutionLine> lineConsumer) {
        executionLines.forEach(lineConsumer::accept);
    }

}
