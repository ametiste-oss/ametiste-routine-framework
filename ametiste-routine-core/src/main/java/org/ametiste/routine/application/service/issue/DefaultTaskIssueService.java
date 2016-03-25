package org.ametiste.routine.application.service.issue;

import org.ametiste.routine.application.CoreEventsGateway;
import org.ametiste.routine.application.TaskDomainEvenetsGateway;
import org.ametiste.routine.domain.scheme.TaskBuilder;
import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.domain.scheme.TaskSchemeException;
import org.ametiste.routine.domain.scheme.TaskSchemeRepository;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.application.events.TaskIssuedEvent;
import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.domain.task.properties.TaskPropertiesRegistry;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.sdk.application.service.issue.constraints.IssueConstraint;
import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;


public class DefaultTaskIssueService implements TaskIssueService {

    private TaskRepository taskRepository;

    private TaskPropertiesRegistry taskPropertiesRegistry;

    private TaskSchemeRepository taskSchemeRepository;

    private final TaskDomainEvenetsGateway taskDomainEvenetsGateway;

    private final CoreEventsGateway coreEventsGateway;
    private final List<IssueConstraint> issueConstraints;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public DefaultTaskIssueService(TaskRepository taskRepository,
                                   TaskPropertiesRegistry taskPropertiesRegistry,
                                   TaskSchemeRepository taskSchemeRepository,
                                   TaskDomainEvenetsGateway taskDomainEvenetsGateway,
                                   CoreEventsGateway coreEventsGateway,
                                   List<IssueConstraint> issueConstraints) {
        this.taskRepository = taskRepository;
        this.taskSchemeRepository = taskSchemeRepository;
        this.taskDomainEvenetsGateway = taskDomainEvenetsGateway;
        this.coreEventsGateway = coreEventsGateway;
        this.issueConstraints = issueConstraints;
    }

    @Override
    public <T extends ParamsProtocol> UUID issueTask(
            Class<? extends TaskScheme<T>> taskSchemeClass, Consumer<T> paramsInstaller, String creatorIdenifier) {

        final TaskBuilder<T> builder = new TaskBuilder<>(taskSchemeRepository, creatorIdenifier);

        final Task task = builder.defineScheme(taskSchemeClass, paramsInstaller)
                .addProperty(Task.SCHEME_PROPERTY_NAME, taskSchemeClass.getName())
                .addProperty(Task.CREATOR_PROPERTY_NAME, creatorIdenifier)
                .build();

        taskRepository.saveTask(task);
        taskDomainEvenetsGateway.taskIssued(task.entityId());
        coreEventsGateway.taskIssued(new TaskIssuedEvent(task.entityId(), creatorIdenifier));

        if (logger.isDebugEnabled()) {
            logger.debug("New task created using scheme:{}, task id: {}, creator id: {}",
                    taskSchemeClass.getSimpleName(), task.entityId().toString(), creatorIdenifier);
        }

        return task.entityId();
    }

}
