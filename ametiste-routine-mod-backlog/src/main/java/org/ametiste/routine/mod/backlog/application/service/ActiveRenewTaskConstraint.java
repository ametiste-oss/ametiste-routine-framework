package org.ametiste.routine.mod.backlog.application.service;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.mod.backlog.application.scheme.BacklogRenewTaskScheme;
import org.ametiste.routine.mod.backlog.domain.Backlog;
import org.ametiste.routine.mod.tasklog.domain.TaskLogRepository;

import java.util.Arrays;

/**
 * <p>
 *     Constraints backlog renew untill <b>all currently active backlog renew tasks</b> is done. Allows
 *     to control race condition during long renew operations.
 * </p>
 *
 * @since 0.1.0
 */
public class ActiveRenewTaskConstraint implements BacklogRenewConstraint {

    private final TaskLogRepository taskLogRepository;

    public ActiveRenewTaskConstraint(TaskLogRepository taskLogRepository) {
        this.taskLogRepository = taskLogRepository;
    }

    @Override
    public boolean isApplicable(Backlog backlog) {

        final long activeCount = taskLogRepository.countByTaskState(
                Task.State.activeState,
                Arrays.asList(
                        new TaskProperty(Task.SCHEME_PROPERTY_NAME, BacklogRenewTaskScheme.NAME)
                )
        );

        return activeCount > 0;

    }

}
