package org.ametiste.routine.application.service.issue;

import org.ametiste.routine.application.CoreEventsGateway;
import org.ametiste.routine.application.TaskDomainEvenetsGateway;
import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.domain.scheme.TaskSchemeException;
import org.ametiste.routine.domain.scheme.TaskSchemeRepository;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.application.events.TaskIssuedEvent;
import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.domain.task.properties.TaskPropertiesRegistry;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.sdk.application.service.issue.constraints.IssueConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;


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
    public UUID issueTask(String taskSchemeName, Map<String, String> params, String creatorIdenifier) {


        final TaskScheme taskScheme = taskSchemeRepository.findTaskScheme(taskSchemeName);

        final Task task;

        // TODO: add aggregate instant for this
        try {
            task = taskScheme.createTask(params, creatorIdenifier);
        } catch (TaskSchemeException e) {
            // TODO: add specific app exception
            throw new RuntimeException("Task creation error", e);
        }

        task.addProperty(new TaskProperty(Task.SCHEME_PROPERTY_NAME, taskSchemeName));
        task.addProperty(new TaskProperty(Task.CREATOR_PROPERTY_NAME, creatorIdenifier));

        taskRepository.saveTask(task);
        taskDomainEvenetsGateway.taskIssued(task.entityId());
        coreEventsGateway.taskIssued(new TaskIssuedEvent(task.entityId(), creatorIdenifier));

        if (logger.isDebugEnabled()) {
            logger.debug("New task created using scheme:{}, task id: {}, creator id: {}",
                    taskScheme, task.entityId().toString(), creatorIdenifier);
        }

        return task.entityId();
    }

}
