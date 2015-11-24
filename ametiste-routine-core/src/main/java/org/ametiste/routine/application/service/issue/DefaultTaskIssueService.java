package org.ametiste.routine.application.service.issue;

import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.domain.scheme.TaskSchemeRepository;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.domain.task.properties.TaskPropertiesRegistry;
import org.ametiste.routine.sdk.application.service.issue.constraints.IssueConstraint;
import org.ametiste.routine.sdk.application.service.task.TaskAppEvenets;

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
    public UUID issueTask(String taskSchemeName, Map<String, String> params) {
        final TaskScheme taskScheme = taskSchemeRepository.findTaskScheme(taskSchemeName);
        final Task task = taskScheme.createTask(params);
        taskRepository.saveTask(task);
        taskAppEvenets.taskIssued(task.entityId());
        return task.entityId();
    }

}
