package org.ametiste.routine.application.service.termination;

import org.ametiste.domain.AggregateInstant;
import org.ametiste.routine.application.CoreEventsGateway;
import org.ametiste.routine.application.TaskDomainEvenetsGateway;
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
    private final CoreEventsGateway coreEventsGateway;

    public DefaultTaskTerminationService(TaskRepository repository,
                                         TaskDomainEvenetsGateway evenetsGateway,
                                         CoreEventsGateway coreEventsGateway) {
        this.repository = repository;
        this.evenetsGateway = evenetsGateway;
        this.coreEventsGateway = coreEventsGateway;
    }

    @Override
    public void terminateTask(UUID taskId, String withMessage) {
        AggregateInstant.create(taskId, repository::findTask, repository::saveTask)
                .action(t -> { return t.terminate(withMessage); })
                .consume(e -> e
                    .taskTerminated(evenetsGateway::taskTerminated)
                    .taskTerminated(coreEventsGateway::taskTerminated)
                    .consume()
                )
                .done();
    }

}
