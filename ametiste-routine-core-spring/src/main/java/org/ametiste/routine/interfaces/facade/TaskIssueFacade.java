package org.ametiste.routine.interfaces.facade;

import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 *
 * <p>
 *     Spring-TX enchanted facade, allow to use {@link TaskIssueService} within
 *     the context transactions.
 * </p>
 *
 * @since 0.0.1
 */
public class TaskIssueFacade {

    private final TaskIssueService taskIssueService;

    public TaskIssueFacade(TaskIssueService taskIssueService) {
        this.taskIssueService = taskIssueService;
    }

    @Transactional
    @Deprecated
    public UUID issueTask(Object taskForm) {
        throw new IllegalStateException("deprecated operation");
    }

}
