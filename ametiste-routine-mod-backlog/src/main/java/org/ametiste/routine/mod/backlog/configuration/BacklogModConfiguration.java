package org.ametiste.routine.mod.backlog.configuration;

import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.configuration.AmetisteRoutineCoreConfiguration;
import org.ametiste.routine.infrastructure.mod.ModDataRepository;
import org.ametiste.routine.mod.backlog.application.action.BacklogRenewAction;
import org.ametiste.routine.mod.backlog.application.operation.BacklogRenewOperationExecutor;
import org.ametiste.routine.mod.backlog.application.service.BacklogRenewService;
import org.ametiste.routine.mod.backlog.domain.Backlog;
import org.ametiste.routine.mod.backlog.domain.BacklogRepository;
import org.ametiste.routine.mod.backlog.domain.RenewSchemeExecutor;
import org.ametiste.routine.mod.backlog.infrasturcture.*;
import org.ametiste.routine.mod.tasklog.domain.TaskLogRepository;
import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @since
 */
@Configuration
@ConditionalOnProperty(prefix = "org.ametiste.routine", name = "mod.backlog.enabled", matchIfMissing = true)
public class BacklogModConfiguration {

    @Autowired
    private TaskLogRepository taskLogRepository;

    @Autowired
    private TaskIssueService taskIssueService;

    @Autowired(required = false)
    private List<Backlog> backlogs = new ArrayList<>();

    @Bean(name = BacklogRenewOperationExecutor.NAME)
    public OperationExecutor backlogRenewOperationExecutor() {
        return new BacklogRenewOperationExecutor();
    }

    @Bean
    public BacklogRenewService backlogRenewService() {
        return new BacklogRenewService(taskLogRepository, taskIssueService);
    }

    @Bean
    public BacklogRepository backlogRepository() {
        return new MemoryBacklogRepository(backlogs);
    }

    @Bean
    public BacklogRenewAction backlogRenewAction() {
        return new BacklogRenewAction(backlogRepository(), backlogRenewService());
    }

}
