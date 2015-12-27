package org.ametiste.routine.application.service.removing;

import org.ametiste.routine.application.CoreEventsGateway;
import org.ametiste.routine.application.events.TasksRemovedEvent;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @since
 */
public class DefaultTaskRemovingService implements TaskRemovingService {

    private final TaskRepository taskRepository;
    private final CoreEventsGateway coreEventsGateway;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public DefaultTaskRemovingService(TaskRepository taskRepository,
                                      CoreEventsGateway coreEventsGateway) {
        this.taskRepository = taskRepository;
        this.coreEventsGateway = coreEventsGateway;
    }

    @Override
    public long removeTasks(final List<Task.State> states, final Instant after, final String clientId) {

        if (!Task.State.completeState.containsAll(states)) {
             throw new IllegalArgumentException("Only completed task can be romved using this service.");
        }

        final long taskCountToRemove = taskRepository.countTasks(c -> {
                    c.stateIn(states.stream().map(Task.State::name).collect(Collectors.toList()));
                    c.completionTimeAfter(after);
                }
        );

        logger.debug("Tasks scheduled for removing by {} : {}", clientId, taskCountToRemove);

        if (taskCountToRemove > 0) {
            taskRepository.deleteTasks(states, after);
            coreEventsGateway.tasksRemoved(new TasksRemovedEvent(taskCountToRemove, clientId));
        }

        return taskCountToRemove;
    }


}
