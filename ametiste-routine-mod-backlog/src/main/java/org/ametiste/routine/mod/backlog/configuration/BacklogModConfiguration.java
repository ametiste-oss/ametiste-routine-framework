package org.ametiste.routine.mod.backlog.configuration;

import org.ametiste.routine.RoutineCoreSpring;
import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.mod.backlog.application.action.BacklogRenewAction;
import org.ametiste.routine.mod.backlog.application.service.ActiveBacklogTasksConstraint;
import org.ametiste.routine.mod.backlog.application.service.ActiveRenewTaskConstraint;
import org.ametiste.routine.mod.backlog.application.service.BacklogRenewConstraint;
import org.ametiste.routine.mod.backlog.application.service.BacklogRenewService;
import org.ametiste.routine.mod.backlog.domain.Backlog;
import org.ametiste.routine.mod.backlog.domain.BacklogRepository;
import org.ametiste.routine.mod.backlog.infrastructure.BacklogPopulationStrategiesRegistry;
import org.ametiste.routine.mod.backlog.infrastructure.BacklogPopulationStrategy;
import org.ametiste.routine.mod.backlog.infrastructure.MemoryBacklogPopulationStrategiesRegistry;
import org.ametiste.routine.mod.backlog.infrastructure.MemoryBacklogRepository;
import org.ametiste.routine.mod.backlog.mod.ModBacklog;
import org.ametiste.routine.mod.tasklog.domain.TaskLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @since
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty(prefix = RoutineCoreSpring.MOD_PROPS_PREFIX,
        name = "backlog.enabled", matchIfMissing = true)
public class BacklogModConfiguration {

    @Autowired
    private TaskLogRepository taskLogRepository;

    @Autowired
    private TaskIssueService taskIssueService;

    @Autowired(required = false)
    private List<Backlog> backlogs = Collections.emptyList();

    @Autowired(required = false)
    private List<BacklogRenewConstraint> constraints = Collections.emptyList();

    @Autowired(required = false)
    private Map<String, BacklogPopulationStrategy> backlogPopulationStrategies = Collections.emptyMap();

    @Value("${org.ametiste.routine.mod.backlog.renewRate:60000}")
    private Long renewRate;

    @Bean
    public BacklogPopulationStrategiesRegistry backlogPopulationStrategiesRegistry() {
        return new MemoryBacklogPopulationStrategiesRegistry(
                backlogPopulationStrategies
        );
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
         return new ModBacklog(renewRate);
    }

    @Bean
    public BacklogRenewConstraint activeRenewTaskConstraint() {
        return new ActiveRenewTaskConstraint(taskLogRepository);
    }

    @Bean
    public BacklogRenewConstraint activeBacklogTaskConstraint() {
        return new ActiveBacklogTasksConstraint(taskLogRepository);
    }

    @Configuration
    @ConditionalOnProperty(name = "org.ametiste.routine.mod.backlog.useCron", havingValue = "false", matchIfMissing = true)
    static class FixedRateRenewConfiguration {

        @Autowired
        private BacklogRenewAction backlogRenewAction;

        @Scheduled(fixedRateString = "${org.ametiste.routine.mod.backlog.renewRate:60000}")
        private void renew() {
            backlogRenewAction.renewAll();
        }
    }

    @Configuration
    @ConditionalOnProperty(name = "org.ametiste.routine.mod.backlog.useCron", havingValue = "true")
    static class CronRenewConfiguration {

        @Autowired
        private BacklogRenewAction backlogRenewAction;

        @Scheduled(cron = "${org.ametiste.routine.mod.backlog.renewCron:0 * * * * *}")
        private void renew() {
            backlogRenewAction.renewAll();
        }
    }
}
