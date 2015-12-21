package org.ametiste.routine.infrastructure.protocol.taskcontrol;

import org.ametiste.routine.application.service.termination.TaskTerminationService;
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

    private final TaskTerminationService taskTerminationService;

    public DirectTaskControlConnection(final String clientId, final TaskTerminationService taskTerminationService) {
        this.clientId = clientId;
        this.taskTerminationService = taskTerminationService;
    }

    @Override
    public void terminateTask(final UUID taskId, final Optional<String> message, final Optional<Consumer<Exception>> failure) {
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
