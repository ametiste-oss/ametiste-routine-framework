package org.ametiste.routine.configuration;

import org.ametiste.routine.RoutineCoreSpring;
import org.ametiste.routine.application.TaskDomainEvenetsGateway;
import org.ametiste.routine.application.service.issue.DefaultTaskIssueService;
import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.application.service.removing.DefaultTaskRemovingService;
import org.ametiste.routine.application.service.removing.TaskRemovingService;
import org.ametiste.routine.application.service.termination.DefaultTaskTerminationService;
import org.ametiste.routine.application.service.termination.TaskTerminationService;
import org.ametiste.routine.domain.ModReportRepository;
import org.ametiste.routine.domain.ModRepository;
import org.ametiste.routine.domain.scheme.SchemeRepository;
import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.domain.task.properties.TaskPropertiesRegistry;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.stat.configuration.CoreStatConfiguration;
import org.ametiste.routine.infrastructure.messaging.JmsTaskDomainEventsGateway;
import org.ametiste.routine.infrastructure.messaging.SpringCoreEventsGateway;
import org.ametiste.routine.infrastructure.mod.InMemoryModReportRepository;
import org.ametiste.routine.infrastructure.mod.SpringDataModRepository;
import org.ametiste.routine.infrastructure.mod.jpa.JPAModDataRepository;
import org.ametiste.routine.stat.interfaces.metrics.InfoMetrics;
import org.ametiste.routine.interfaces.web.task.TaskController;
import org.ametiste.routine.sdk.application.service.issue.constraints.IssueConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.core.JmsTemplate;

import java.util.List;

@Configuration
@ComponentScan(basePackageClasses =
    {
        TaskController.class,   // enables scan for info web components
        InfoMetrics.class,      // enables scan for info metric source components
        RoutineCoreSpring.class // enables scan for framework components in case when host application
                                // placed not in root routine package
    }
)
@EntityScan(basePackageClasses = RoutineCoreSpring.class)
@EnableJpaRepositories(basePackageClasses = RoutineCoreSpring.class)
@EnableConfigurationProperties(AmetisteRoutineCoreProperties.class)
@Import(CoreStatConfiguration.class)
@PropertySource(RoutineCoreSpring.ROUTINE_CORE_PROPERTIES)
public class AmetisteRoutineCoreConfiguration {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired(required = false)
    private List<IssueConstraint> issueConstraints;

    @Autowired
    private TaskPropertiesRegistry taskPropertiesRegistry;

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private JPAModDataRepository modDataRepository;

    @Autowired
    private AmetisteRoutineCoreProperties props;

    @Autowired
    private ApplicationEventPublisher springEventPublisher;

    @Bean
    public SpringCoreEventsGateway springCoreEventsGateway() {
        return new SpringCoreEventsGateway(springEventPublisher);
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskTerminationService taskExecutionService() {
        return new DefaultTaskTerminationService(
                taskRepository, domainEventsGateway(), springCoreEventsGateway());
    }

    @Bean
    public TaskIssueService taskIssueService() {
        return new DefaultTaskIssueService(taskRepository, taskPropertiesRegistry,
                schemeRepository, domainEventsGateway(), springCoreEventsGateway(), issueConstraints);
    }

    @Bean
    public TaskRemovingService taskRemovingService() {
        return new DefaultTaskRemovingService(taskRepository, springCoreEventsGateway());
    }

    @Bean
    public ModRepository modDataRepository() {
        return new SpringDataModRepository(modDataRepository);
    }

    @Bean
    public ModReportRepository modReportRepository() {
        return new InMemoryModReportRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskDomainEvenetsGateway domainEventsGateway() {
        return new JmsTaskDomainEventsGateway(jmsTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskPropertiesRegistry taskPropertiesRegistry() {
        return new TaskPropertiesRegistry() {
            @Override
            public TaskProperty createTaskProperty(String kind, String value) {
                return null;
            }
        };
    }

}
