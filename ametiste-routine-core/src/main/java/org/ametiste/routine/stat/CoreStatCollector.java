package org.ametiste.routine.stat;

import org.ametiste.routine.application.events.TaskIssuedEvent;
import org.ametiste.routine.application.events.TasksRemovedEvent;
import org.ametiste.routine.domain.task.TaskDoneEvent;
import org.ametiste.routine.domain.task.TaskTerminatedEvent;

/**
 *
 * @since
 */
public class CoreStatCollector
        implements TerminatedTasksStatCollector, DoneTasksStatCollector,
        IssuedTasksStatCollector, RemovedTasksStatCollector, CoreStats {

    private final CoreStatRepository coreStatRepository;

    public CoreStatCollector(CoreStatRepository coreStatRepository) {
        this.coreStatRepository = coreStatRepository;
    }

    @Override
    public void onTaskDone(final TaskDoneEvent event) {
        coreStatRepository.incrementStat(STAT_DONE);
    }

    @Override
    public void onTaskIssued(final TaskIssuedEvent event) {
        coreStatRepository.incrementStat(STAT_ISSUED);
    }

    @Override
    public void onTaskTerminated(final TaskTerminatedEvent event) {
        coreStatRepository.incrementStat(STAT_TERMINATED);
    }

    @Override
    public void onTasksRemoved(final TasksRemovedEvent event) {
        coreStatRepository.incrementStat(STAT_REMOVED, event.getTaskCountToRemove());
    }
}
