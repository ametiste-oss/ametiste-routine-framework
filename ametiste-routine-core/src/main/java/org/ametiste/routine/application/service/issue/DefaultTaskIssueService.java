package org.ametiste.routine.application.service.issue;

import org.ametiste.routine.domain.scheme.TaskCreationRejectedBySchemeException;
import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.domain.scheme.TaskSchemeRepository;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.domain.task.properties.TaskPropertiesRegistry;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.sdk.application.service.issue.constraints.IssueConstraint;
import org.ametiste.routine.application.service.TaskAppEvenets;

import java.util.List;
import java.util.Map;
import java.util.UUID;


public class DefaultTaskIssueService implements TaskIssueService {

    private TaskRepository taskRepository;

    private TaskPropertiesRegistry taskPropertiesRegistry;

    private TaskSchemeRepository taskSchemeRepository;

    private final TaskAppEvenets taskAppEvenets;

    private final List<IssueConstraint> issueConstraints;

    public DefaultTaskIssueService(TaskRepository taskRepository,
                                   TaskPropertiesRegistry taskPropertiesRegistry,
                                   TaskSchemeRepository taskSchemeRepository,
                                   TaskAppEvenets taskAppEvenets,
                                   List<IssueConstraint> issueConstraints) {
        this.taskRepository = taskRepository;
        this.taskSchemeRepository = taskSchemeRepository;
        this.taskAppEvenets = taskAppEvenets;
        this.issueConstraints = issueConstraints;
    }

    @Override
    public UUID issueTask(String taskSchemeName, Map<String, String> params, String creatorIdenifier) {

        final TaskScheme taskScheme = taskSchemeRepository.findTaskScheme(taskSchemeName);

        final Task task;

        try {
            task = taskScheme.createTask(params, creatorIdenifier);
        } catch (TaskCreationRejectedBySchemeException e) {
            // TODO: add specific app exception
            throw new RuntimeException("Task creation rejected by scheme.", e);
        }

        task.addProperty(new TaskProperty(Task.SCHEME_PROPERTY_NAME, taskSchemeName));
        task.addProperty(new TaskProperty(Task.CREATOR_PROPERTY_NAME, creatorIdenifier));

        taskRepository.saveTask(task);
        taskAppEvenets.taskIssued(task.entityId());

        return task.entityId();
    }

}
