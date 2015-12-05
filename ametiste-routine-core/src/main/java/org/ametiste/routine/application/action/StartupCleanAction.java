package org.ametiste.routine.application.action;

import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.TaskRepository;

import java.util.List;

/**
 * <p>
 *  This action is designed to prevent tasks of hangin in abnormal states in case of application restarts.
 * </p>
 *
 * <p>
 *     Action assumed all incomlete tasks are failed, so if retry policy is used, these actions will
 *     be retry immediately.
 * </p>
 *
 * @since 0.1.0
 */
public class StartupCleanAction implements Runnable {

    private TaskRepository taskRepository;

    public StartupCleanAction(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void run() {
        // TODO: need to rework it, now it is implemented as sequential reader to
        // prevent huge tasks bucket load.
        while(true) {

            final List<Task> tasks =
                    taskRepository.findTasksByState(Task.State.activeStatesList, 100);

            if (tasks.size() == 0) {
                break;
            }

            tasks.forEach(
                    task -> {
                        task.terminate("Active during startup, terminated by StartupCleanAction.");
                        taskRepository.saveTask(task);
                    }
            );
        }
    }

}
