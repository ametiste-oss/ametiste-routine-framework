package org.ametiste.routine.application.service.execution;

import org.ametiste.routine.domain.task.ExecutionLine;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @since
 */
public interface LineExecutionGateway {

    void executeOperation(ExecutionLine line);

}
