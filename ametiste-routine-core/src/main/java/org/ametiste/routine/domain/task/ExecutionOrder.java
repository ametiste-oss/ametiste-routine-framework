package org.ametiste.routine.domain.task;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @since
 */
public class ExecutionOrder {

    private final Collection<ExecutionLine> executionLines;

    public ExecutionOrder(List<ExecutionLine> executionLines) {
        this.executionLines = Collections.unmodifiableCollection(executionLines);
    }

    public Collection<ExecutionLine> executionLines() {
        return executionLines;
    }
}
