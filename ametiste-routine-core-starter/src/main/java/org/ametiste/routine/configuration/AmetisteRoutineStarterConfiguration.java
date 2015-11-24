package org.ametiste.routine.configuration;

import org.ametiste.routine.application.service.execution.DefaultTaskExecutionService;
import org.ametiste.routine.application.service.execution.ExecutorManager;
import org.ametiste.routine.application.service.execution.TaskExecutionService;
import org.ametiste.routine.application.service.issue.DefaultTaskIssueService;
import org.ametiste.routine.application.service.issue.TaskIssueService;
import org.ametiste.routine.domain.scheme.TaskSchemeRepository;
import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.domain.task.properties.TaskPropertiesRegistry;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.sdk.application.service.execution.ExecutionManager;
import org.ametiste.routine.sdk.application.service.execution.OperationExecutorFactory;
import org.ametiste.routine.sdk.application.service.issue.constraints.IssueConstraint;
import org.ametiste.routine.sdk.application.service.task.TaskAppEvenets;
import org.ametiste.routine.infrastructure.messaging.JmsIssuedTaskEventListener;
import org.ametiste.routine.infrastructure.messaging.JmsTaskAppEvents;
import org.ametiste.routine.interfaces.web.TaskController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import java.util.List;
import java.util.Map;

@Configuration
@Import({JdbcTaskRepositoryConfiguration.class,TaskSchemeRepositoryConfiguration.class})
@ComponentScan(basePackageClasses = {TaskController.class})
public class AmetisteRoutineStarterConfiguration {

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

    @Autowired(required = false)
    private Map<String, OperationExecutorFactory> operationExecutors;

    @Bean
    public TaskIssueService taskIssueService() {
        return new DefaultTaskIssueService(taskRepository, taskPropertiesRegistry,
                taskSchemeRepository, taskAppEvenets(), issueConstraints);
    }

    @Bean
    @ConditionalOnMissingBean
    public ExecutionManager executionManager() {
        return new ExecutorManager(operationExecutors);
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskExecutionService taskExecutionService() {
        return new DefaultTaskExecutionService(taskRepository, executionManager());
    }

    @Bean
    public JmsListenerContainerFactory<?> issuedTasksListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency("5");
        return factory;
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskAppEvenets taskAppEvenets() {
        return new JmsTaskAppEvents(jmsTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public JmsIssuedTaskEventListener issuedTaskEventListener() {
        return new JmsIssuedTaskEventListener(taskExecutionService());
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
