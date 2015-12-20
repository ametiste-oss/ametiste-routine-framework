package org.ametiste.routine.infrastructure.protocol.taskcontrol;

import org.ametiste.routine.application.service.execution.TaskTerminationService;
import org.ametiste.routine.sdk.mod.TaskControlProtocol;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/**
 *
 * @since
 */
public class DirectTaskControlConnection implements TaskControlProtocol {

    private final String clientId;
    private TaskTerminationService taskTerminationService;

    public DirectTaskControlConnection(String clientId, TaskTerminationService taskTerminationService) {
        this.clientId = clientId;
        this.taskTerminationService = taskTerminationService;
    }

    @Override
    public void terminateTask(UUID taskId, Optional<String> message, Optional<Consumer<Exception>> failure) {
        try {
            taskTerminationService.terminateTask(taskId,
                    message.orElse("Completed using DirectTaskControlConnection by: " + clientId)
            );
        } catch (Exception e) {
            failure.ifPresent(
                (exceptionConsumer -> exceptionConsumer.accept(e))
            );
        }
    }

}
