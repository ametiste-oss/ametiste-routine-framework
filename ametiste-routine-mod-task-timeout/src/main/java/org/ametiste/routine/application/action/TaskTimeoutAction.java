package org.ametiste.routine.application.action;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.mod.tasklog.mod.TaskLogProtocol;
import org.ametiste.routine.sdk.protocol.taskcontrol.TaskControlProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * <p>
 *     This action allow to check timed out tasks and mark them as 'TERMINATED'.
 * </p>
 *
 * @since 0.1.0
 */
public class TaskTimeoutAction {

    private long defaultTimeout;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public TaskTimeoutAction(long defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    public void executeAction(ProtocolGateway protocolGateway) {

        // TODO: need to add entry to global ApplicationEventsLog that reports about timed out entities

        if (logger.isDebugEnabled()) {
            logger.debug("Trying to execute timeouts.");
        }


        // TODO: full scan, not only first 100?
        protocolGateway.session(TaskLogProtocol.class)
            .findIdentifiers(f -> {
                f.stateIn(Task.State.executionState);
                f.execStartTimeAfter(Instant.now().minus(defaultTimeout, ChronoUnit.SECONDS));
            }, 0, 100)
            .forEach(taskId -> {

                if (logger.isDebugEnabled()) {
                    logger.debug("Exectuting timeout for : " + taskId);
                }

                try {
                    protocolGateway.session(TaskControlProtocol.class)
                            .terminateTask(taskId, String.format("Timedout for %s minutes.", defaultTimeout));
                } catch (Exception e) {
                    // NOTE: should try to terminate all timedout tasks.
                    logger.debug("Exception during task timeout termination : " + taskId, e);
                }
        });

    }

}
