package org.ametiste.routine.infrastructure.persistency.memory;

import org.ametiste.routine.domain.log.TaskLogEntry;
import org.ametiste.routine.domain.log.TaskLogNotFoundException;
import org.ametiste.routine.domain.log.TaskLogRepository;
import org.ametiste.routine.domain.task.Task;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @since
 */
public class InMemoryTaskLogRepository implements TaskLogRepository {

    private HashMap<UUID, TaskLogEntry> log = new HashMap<>();

    @Override
    public long countActiveTasks() {

        return log.values().stream()
            .filter((e)->{ return e.getState().equals(Task.State.PENDING.name()) ||
                    e.getState().equals(Task.State.EXECUTION.name());})
            .count();

    }

    @Override
    public List<UUID> findNewTasks(long appendCount) {

        final ArrayList<UUID> entries = new ArrayList<>();

        for (TaskLogEntry logEntry : log.values()) {
            if (logEntry.getState().equals(Task.State.NEW.name())) {
                entries.add(logEntry.getTaskId());
            }

            if (entries.size() == appendCount) {
                break;
            }

        }

        return entries;
    }

    @Override
    public void saveTaskLog(TaskLogEntry taskLogEntry) {
        log.put(taskLogEntry.getTaskId(), taskLogEntry);
    }

    @Override
    public List<TaskLogEntry> findEntries() {
        return Collections.unmodifiableList(new ArrayList<TaskLogEntry>(log.values()));
    }

    @Override
    public TaskLogEntry findTaskLog(UUID taskId) {
        if (log.containsKey(taskId)) {
            return log.get(taskId);
        } else {
            throw new TaskLogNotFoundException("Can't find log for the task with the given id : " + taskId);
        }
    }

    @Override
    public List<UUID> findActiveTasksAfterDate(Instant timePoint) {

        return log.values().stream()

            // TODO: add nice check method to the log object
            //
            // .filter((e) -> {return e.startTimeIs(timePoint);})
            // .filter((e) -> {return e.stateIs(OpState.EXECUTION);})
            //

            .filter((e) -> {
                return e.getExecutionStartTime().isPresent() &&
                        e.getExecutionStartTime().get().isBefore(timePoint);
            })

            .filter((e) -> {
                return e.getState().equals(Task.State.EXECUTION.name()) ||
                        e.getState().equals(Task.State.PENDING.name());
            })
            .map(e -> e.getTaskId())
            .collect(Collectors.toList());

    }

    @Override
    public List<TaskLogEntry> findEntries(String byStatus, int offset, int limit) {
        return log.values().stream()

                // TODO: add nice check method to the log object
                //
                // .filter((e) -> {return e.startTimeIs(timePoint);})
                // .filter((e) -> {return e.stateIs(OpState.EXECUTION);})
                //

                .filter((e) -> {
                    return e.getState().equals(byStatus);
                })

                .collect(Collectors.toList());
    }

    @Override
    public int countEntriesByStatus(String byStatus) {
        return 0;
    }

}
