package org.ametiste.routine.mod.dispenser.application.service;

import org.ametiste.routine.domain.task.ExecutionLine;
import org.ametiste.routine.domain.task.ExecutionOrder;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *     Basic implementation of the {@link OperationReservationService} which implemented
 *     using core {@link org.ametiste.routine.domain.task.TaskRepository} service to
 *     find and save reserved operations.
 * </p>
 *
 * <p>
 *     Service implementation provides ability to restrict count of tasks that could
 *     be reserved by one request.
 * </p>
 *
 * <p>
 *     <b>NOTE:</b> this is very simple implementation, its count each task as single operation,
 *     it means if the task constains more then one operation - all operations
 *     of a task will be reserved as single reservation.
 *     <br>
 *     So, this implementation may be used only in the environments where tasks has single operation,
 *     or where all tasks has same amout of operations. In the environments where tasks has broad structure
 *     it was hard to predict reservation density.
 *     <br>
 *     <b>To constraint this behaviour</b>, task-related constraint may be used, this constaint should
 *     match operations count within the task, and allow to reserve only tasks which has only one operation included.
 * </p>
 *
 * <p>
 *     TODO: Another implementation which would allow to reserve only requested number of operations.<br>
 *     TODO: To support this feature, the domain should allow to launch tasks partialy,<br>
 *     TODO: atm all operations of the tasks become 'PENDING' after {@link Task#prepareExecution()} call.<br>
 * </p>
 *
 * <p>
 *     TODO: Also, total amount of reserved tasks could be limited. [ Constraint ]
 * </p>
 *
 * <p>
 *     TODO: add task-related constraints, which would allow to setup constraints to check tasks consitensy
 * </p>
 *
 * @since 0.0.1
 */
public class DefaultOperationReservationService implements OperationReservationService {

    public final static int DEFAULT_RESERVATION_LIMIT = 10;

    private final int reservationLimit;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TaskRepository taskRepository;

    public DefaultOperationReservationService(TaskRepository taskRepository) {
        this(taskRepository, DEFAULT_RESERVATION_LIMIT);
    }

    public DefaultOperationReservationService(TaskRepository taskRepository, int reservationLimit) {
        this.taskRepository = taskRepository;
        this.reservationLimit = reservationLimit;
    }

    @Override
    @Transactional
    public List<ExecutionLine> reserveOperationsExecution(int reservationCount) {

        final List<Task> tasks = taskRepository
                .findTasksByState(Task.State.NEW, reservationCount);

        if (logger.isDebugEnabled()) {
            logger.debug("Reserved tasks count: " + tasks.size());
        }

        final List<ExecutionLine> executionLines = tasks.stream()
                .map(Task::prepareExecution)
                .map(ExecutionOrder::executionLines)
                .flatMap(l -> l.stream())
                .collect(Collectors.toList());

        tasks.forEach(taskRepository::saveTask);

        return executionLines;
    }

}
