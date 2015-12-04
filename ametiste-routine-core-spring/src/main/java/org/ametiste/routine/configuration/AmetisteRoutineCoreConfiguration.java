package org.ametiste.routine.configuration;

import org.ametiste.routine.application.service.execution.DefaultTaskExecutionService;
import org.ametiste.routine.infrastructure.execution.DefaultOperationExecutionGateway;
import org.ametiste.routine.application.service.issue.DefaultTaskIssueService;
import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.domain.scheme.TaskSchemeRepository;
import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.domain.task.properties.TaskPropertiesRegistry;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.application.service.execution.OperationExecutionGateway;
import org.ametiste.routine.infrastructure.mod.ModRepository;
import org.ametiste.routine.infrastructure.mod.SpringDataModRepository;
import org.ametiste.routine.infrastructure.mod.jpa.JPAModDataRepository;
import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.ametiste.routine.sdk.operation.OperationExecutorFactory;
import org.ametiste.routine.sdk.application.service.issue.constraints.IssueConstraint;
import org.ametiste.routine.application.service.TaskAppEvenets;
import org.ametiste.routine.infrastructure.messaging.JmsTaskEventsListener;
import org.ametiste.routine.infrastructure.messaging.JmsTaskAppEvents;
import org.ametiste.routine.interfaces.web.TaskController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Import({JdbcTaskRepositoryConfiguration.class,TaskSchemeRepositoryConfiguration.class})
@ComponentScan(basePackageClasses = {TaskController.class})
@EnableConfigurationProperties(AmetisteRoutineCoreProperties.class)
public class AmetisteRoutineCoreConfiguration {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired(required = false)
    private List<IssueConstraint> issueConstraints;

    @Autowired
    private TaskPropertiesRegistry taskPropertiesRegistry;

    @Autowired
    private TaskSchemeRepository taskSchemeRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private JPAModDataRepository modDataRepository;

    @Autowired
    private AmetisteRoutineCoreProperties props;

    @Bean
    public TaskIssueService taskIssueService() {
        return new DefaultTaskIssueService(taskRepository, taskPropertiesRegistry,
                taskSchemeRepository, taskAppEvenets(), issueConstraints);
    }

    @Bean
    public ModRepository modDataRepository() {
        return new SpringDataModRepository(modDataRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskAppEvenets taskAppEvenets() {
        return new JmsTaskAppEvents(jmsTemplate);
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
