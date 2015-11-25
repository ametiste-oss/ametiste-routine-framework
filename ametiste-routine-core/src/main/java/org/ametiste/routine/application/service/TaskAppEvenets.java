package org.ametiste.routine.application.service;

import org.ametiste.routine.domain.task.ExecutionOrder;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 *     General protocol to send various app events through the system.
 * </p>
 *
 * @since 0.1.0
 */
public interface TaskAppEvenets {

    void taskIssued(UUID taskId);

    void taskPended(ExecutionOrder executionOrder);

}
