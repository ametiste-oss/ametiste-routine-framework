package org.ametiste.routine.mod.backlog.application.service;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.mod.backlog.application.operation.BacklogRenewOperationScheme;
import org.ametiste.routine.mod.backlog.domain.Backlog;
import org.ametiste.routine.mod.backlog.mod.ModBacklog;
import org.ametiste.routine.mod.tasklog.domain.TaskLogRepository;

import java.util.Arrays;

/**
 * <p>
 *     Constraints backlog renew untill <b>all tasks of the concrete backlog</b> is done.
 * </p>
 *
 * @since 0.1.0
 */
public class ActiveBacklogTasksConstraint implements BacklogRenewConstraint {

    private final TaskLogRepository taskLogRepository;

    public ActiveBacklogTasksConstraint(TaskLogRepository taskLogRepository) {
        this.taskLogRepository = taskLogRepository;
    }

    @Override
    public boolean isApplicable(Backlog backlog) {

        final long activeCount = taskLogRepository.countByTaskState(
                Task.State.activeState,
                Arrays.asList(
                        // TODO: this properties are required and installed by core services,
                        // TODO Need some kind of constants or something like this
                        new TaskProperty(Task.SCHEME_PROPERTY_NAME, backlog.boundTaskScheme()),
                        new TaskProperty(Task.CREATOR_PROPERTY_NAME, BacklogRenewOperationScheme.NAME)
                )
        );

        return activeCount > 0;
    }
}
