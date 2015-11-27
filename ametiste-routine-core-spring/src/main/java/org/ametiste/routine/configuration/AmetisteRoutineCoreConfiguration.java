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
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
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

    @Autowired(required = false)
    private Map<String, OperationExecutorFactory> operationExecutorFactories = Collections.emptyMap();

    @Autowired(required = false)
    private Map<String, OperationExecutor> operationExecutors = Collections.emptyMap();

    @Autowired
    private AmetisteRoutineCoreProperties props;

    @Bean
    public TaskIssueService taskIssueService() {
        return new DefaultTaskIssueService(taskRepository, taskPropertiesRegistry,
                taskSchemeRepository, taskAppEvenets(), issueConstraints);
    }

    @Bean
    @ConditionalOnMissingBean
    public OperationExecutionGateway operationExecutionGateway() {

        final HashMap<String, OperationExecutorFactory> factories = new HashMap<>();
        factories.putAll(operationExecutorFactories);

        //
        // DOCUMENTATE ME:
        //
        // OperationExecutorFactory allows to control process of OperationExecutor creation,
        // that may be useful when OperationExecutor is stateful or require additional configuration.
        // Also it may be used if new executor instance required for each operation execution.
        //
        // Registered OperationExecutor beans will be adopted to OperationExecutorFactory.
        //
        // Note, be careful to use OperationExecutor shortcut, be shure to use it only for stateless
        // executors.
        //

        //
        // NOTE: adoptation of stateless executors to factories, to shortcut configuration for
        // stateless executors, that anonymous factory will return one instance of an executor for
        // each request.
        //
        operationExecutors.entrySet().stream().forEach(
                (k) -> factories.put(k.getKey(), () -> k.getValue())
        );

        return new DefaultOperationExecutionGateway(factories);
    }

    @Bean
    @ConditionalOnMissingBean
    // NOTE: DefaultTaskExecutionService implements ExecutionFeedback interface also, so we need it as type
    public DefaultTaskExecutionService taskExecutionService() {
        return new DefaultTaskExecutionService(taskRepository, taskAppEvenets(), operationExecutionGateway());
    }

    @Bean
    public JmsListenerContainerFactory<?> issuedTasksListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency(Integer.toString(props.getInitialExecutionConcurrency()));
        return factory;
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskAppEvenets taskAppEvenets() {
        return new JmsTaskAppEvents(jmsTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public JmsTaskEventsListener issuedTaskEventListener() {
        return new JmsTaskEventsListener(taskExecutionService(),
                taskExecutionService(),  // NOTE: DefaultTaskExecutionService implements ExecutionFeedback interface
                operationExecutionGateway()
        );
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
