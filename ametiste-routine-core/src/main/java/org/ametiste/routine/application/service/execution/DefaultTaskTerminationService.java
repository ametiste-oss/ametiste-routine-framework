package org.ametiste.routine.application.service.execution;

import org.ametiste.domain.AggregateInstant;
import org.ametiste.routine.application.service.TaskDomainEvenetsGateway;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 *
 * @since
 */
// NOTE: this class methods may throw IllegalStateException,
// since tasks state are eventualy consisten, that is asssumed
// as okay for this version to control tasks state through
// exceptions.
public class DefaultTaskTerminationService implements TaskTerminationService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final TaskRepository repository;
    private final TaskDomainEvenetsGateway evenetsGateway;

    public DefaultTaskTerminationService(TaskRepository repository,
                                         TaskDomainEvenetsGateway evenetsGateway) {
        this.repository = repository;
        this.evenetsGateway = evenetsGateway;
    }

//    @Override
//    public void prepareTaskExecution(UUID taskId) {
//        AggregateInstant.create(taskId, repository::findTask, repository::saveTask)
//                .action(t -> { logger.debug("Prepare task execution: {} ", t.entityId()); })
//                .action(Task::prepareExecution)
//                .consume(evenetsGateway::taskExecutionPrepared)
//                .consume(o -> { logger.debug("Task execution prepared: {}", o.executionLines()); })
//                .done();
//    }

    @Override
    public void terminateTask(UUID taskId, String withMessage) {
        AggregateInstant.create(taskId, repository::findTask, repository::saveTask)
                .action(t -> { return t.terminate(withMessage); })
                .consume(evenetsGateway::taskTerminated)
                .done();
    }

}
