package org.ametiste.routine.mod.backlog.application.service;

import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.mod.backlog.application.operation.BacklogParams;
import org.ametiste.routine.mod.backlog.application.operation.BacklogRenewOperationScheme;
import org.ametiste.routine.mod.backlog.application.scheme.BacklogRenewTaskScheme;
import org.ametiste.routine.mod.backlog.domain.Backlog;
import org.ametiste.routine.mod.backlog.mod.ModBacklog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 *
 * @since 0.1.0
 */
public class BacklogRenewService {

    // TODO: should use protocol
    private final TaskIssueService taskIssueService;

    private final List<BacklogRenewConstraint> constraints;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public BacklogRenewService(TaskIssueService taskIssueService,
                               List<BacklogRenewConstraint> constraints) {
        this.taskIssueService = taskIssueService;
        this.constraints = constraints;
    }

    public void renewBy(Backlog backlog) {

        final Optional<BacklogRenewConstraint> appliedConstraint =
                applyConstraint(backlog);

        if (appliedConstraint.isPresent()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Renew constraint '{}' applied, renew skiped.",
                        appliedConstraint.get().getClass().getName());
            }
            return;
        }

        taskIssueService.issueTask(
            BacklogRenewTaskScheme.class,
            p -> {
                p.schemeName(backlog.boundTaskScheme());
            },
            BacklogRenewOperationScheme.NAME
        );
    }

    private Optional<BacklogRenewConstraint> applyConstraint(Backlog backlog) {

        final ArrayList<BacklogRenewConstraint> backlogRenewConstraints =
                new ArrayList<>(this.constraints);

        backlogRenewConstraints.addAll(backlog.boundConstraints());

        return backlogRenewConstraints
                .stream()
                .filter(c -> c.isApplicable(backlog) == true)
                .findAny();
    }

}
