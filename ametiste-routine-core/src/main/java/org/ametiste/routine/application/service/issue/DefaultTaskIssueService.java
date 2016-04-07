package org.ametiste.routine.application.service.issue;

import org.ametiste.routine.application.CoreEventsGateway;
import org.ametiste.routine.application.TaskDomainEvenetsGateway;
import org.ametiste.routine.domain.scheme.TaskBuilder;
import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.domain.scheme.SchemeRepository;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.application.events.TaskIssuedEvent;
import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.domain.task.properties.TaskPropertiesRegistry;
import org.ametiste.routine.sdk.application.service.issue.constraints.IssueConstraint;
import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class DefaultTaskIssueService implements TaskIssueService {

    private TaskRepository taskRepository;

    private TaskPropertiesRegistry taskPropertiesRegistry;

    private SchemeRepository schemeRepository;

    private final TaskDomainEvenetsGateway taskDomainEvenetsGateway;

    private final CoreEventsGateway coreEventsGateway;
    private final List<IssueConstraint> issueConstraints;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public DefaultTaskIssueService(TaskRepository taskRepository,
                                   TaskPropertiesRegistry taskPropertiesRegistry,
                                   SchemeRepository schemeRepository,
                                   TaskDomainEvenetsGateway taskDomainEvenetsGateway,
                                   CoreEventsGateway coreEventsGateway,
                                   List<IssueConstraint> issueConstraints) {
        this.taskRepository = taskRepository;
        this.schemeRepository = schemeRepository;
        this.taskDomainEvenetsGateway = taskDomainEvenetsGateway;
        this.coreEventsGateway = coreEventsGateway;
        this.issueConstraints = issueConstraints;
    }

    @Override
    public UUID issueTask(final TaskScheme taskScheme, final Consumer<ParamsProtocol> paramsProtocol, final String creatorIdentifier) {

        final TaskBuilder builder = new TaskBuilder<>(schemeRepository, creatorIdentifier)
                .forScheme(taskScheme, paramsProtocol);

        return issueTask(
                taskScheme.schemeName(),
                creatorIdentifier,
                buildTask(builder, taskScheme.schemeName(), creatorIdentifier)
        );
    }

    @Override
    public <T extends ParamsProtocol> UUID issueTask(
            Class<? extends TaskScheme<T>> taskSchemeClass, Consumer<T> paramsInstaller, String creatorIdentifier) {

        final TaskBuilder<T> builder = new TaskBuilder<T>(schemeRepository, creatorIdentifier)
                .defineScheme(taskSchemeClass, paramsInstaller);

        return issueTask(
                taskSchemeClass.getSimpleName(),
                creatorIdentifier,
                buildTask(builder, taskSchemeClass.getSimpleName(), creatorIdentifier)
        );
    }

    private Task buildTask(final TaskBuilder taskBuilder, final String schemeName, final String creatorIdentifier) {
        return taskBuilder.addProperty(Task.SCHEME_PROPERTY_NAME, schemeName)
                .addProperty(Task.CREATOR_PROPERTY_NAME, creatorIdentifier)
                .build();
    }

    private UUID issueTask(final String schemeName, final String creatorIdentifier, final Task task) {

        taskRepository.saveTask(task);
        taskDomainEvenetsGateway.taskIssued(task.entityId());
        coreEventsGateway.taskIssued(new TaskIssuedEvent(task.entityId(), creatorIdentifier));

        if (logger.isDebugEnabled()) {
            logger.debug("New task created using scheme: {}, task id: {}, creator id: {}",
                    schemeName, task.entityId().toString(), creatorIdentifier);
        }

        return task.entityId();
    }

}
