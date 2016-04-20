package org.ametiste.routine.application.service.issue;

import org.ametiste.routine.application.CoreEventsGateway;
import org.ametiste.routine.application.TaskDomainEvenetsGateway;
import org.ametiste.routine.application.events.TaskIssuedEvent;
import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.domain.scheme.SchemeRepository;
import org.ametiste.routine.domain.scheme.TaskBuilder;
import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @since 1.1.
 */
// TODO: нужно имя получше, это сервис который работает не
// с формальными описаниями схем, а с их "свободными" описаниями
public class NamedTaskSchemeService {

    private SchemeRepository schemeRepository;

    private TaskIssueService taskIssueService;

    public NamedTaskSchemeService(final SchemeRepository schemeRepository, final TaskIssueService taskIssueService) {
        this.schemeRepository = schemeRepository;
        this.taskIssueService = taskIssueService;
    }

    public UUID issueTask(final String taskSchemeName, final Map<String, String> params, final String creatorIdentifier) {
        final TaskScheme taskScheme = schemeRepository.findTaskScheme(taskSchemeName);
        return taskIssueService.issueTask(taskScheme, p -> p.fromMap(params), creatorIdentifier);
    }

}
