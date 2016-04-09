package org.ametiste.routine.infrastructure.execution;

import org.ametiste.routine.domain.task.ExecutionLine;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @since
 */
public interface LineExecutionGateway {

    void executeOperation(UUID taskId, ExecutionLine line);

}
