package org.ametiste.routine.mod.backlog.configuration;

import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.configuration.AmetisteRoutineCoreProperties;
import org.ametiste.routine.mod.backlog.application.action.BacklogRenewAction;
import org.ametiste.routine.mod.backlog.application.operation.BacklogRenewOperationExecutor;
import org.ametiste.routine.mod.backlog.application.service.ActiveBacklogTasksConstraint;
import org.ametiste.routine.mod.backlog.application.service.ActiveRenewTaskConstraint;
import org.ametiste.routine.mod.backlog.application.service.BacklogRenewConstraint;
import org.ametiste.routine.mod.backlog.application.service.BacklogRenewService;
import org.ametiste.routine.mod.backlog.domain.Backlog;
import org.ametiste.routine.mod.backlog.domain.BacklogRepository;
import org.ametiste.routine.mod.backlog.infrastructure.*;
import org.ametiste.routine.mod.backlog.mod.ModBacklog;
import org.ametiste.routine.mod.tasklog.domain.TaskLogRepository;
import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @since
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty(prefix = AmetisteRoutineCoreProperties.PREFIX,
        name = "mod.backlog.enabled", matchIfMissing = true)
public class BacklogModConfiguration {

    @Autowired
    private TaskLogRepository taskLogRepository;

    @Autowired
    private TaskIssueService taskIssueService;

    @Autowired(required = false)
    private List<Backlog> backlogs = new ArrayList<>();

    @Autowired
    private List<BacklogRenewConstraint> constraints;

    @Bean(name = BacklogRenewOperationExecutor.NAME)
    public OperationExecutor backlogRenewOperationExecutor() {
        return new BacklogRenewOperationExecutor();
    }

    @Bean
    public BacklogRenewService backlogRenewService() {
        return new BacklogRenewService(taskIssueService, constraints);
    }

    @Bean
    public BacklogRepository backlogRepository() {
        return new MemoryBacklogRepository(backlogs);
    }

    @Bean
    public BacklogRenewAction backlogRenewAction() {
        return new BacklogRenewAction(backlogRepository(), backlogRenewService());
    }

    @Bean
    public ModBacklog backlogModGateway() {
         return new ModBacklog();
    }

    @Bean
    public BacklogRenewConstraint activeRenewTaskConstraint() {
        return new ActiveRenewTaskConstraint(taskLogRepository);
    }

    @Bean
    public BacklogRenewConstraint activeBacklogTaskConstraint() {
        return new ActiveBacklogTasksConstraint(taskLogRepository);
    }

}
