package org.ametiste.routine.configuration;

import org.ametiste.routine.application.service.TaskAppEvenets;
import org.ametiste.routine.application.service.execution.DefaultTaskExecutionService;
import org.ametiste.routine.application.service.execution.OperationExecutionGateway;
import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.infrastructure.execution.DefaultOperationExecutionGateway;
import org.ametiste.routine.infrastructure.messaging.JmsTaskEventsListener;
import org.ametiste.routine.infrastructure.protocol.ProtocolGatewayService;
import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.ametiste.routine.sdk.operation.OperationExecutorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @since
 */
@Configuration
@EnableConfigurationProperties(AmetisteRoutineCoreProperties.class)
public class OperationExecutionConfiguration {

    @Autowired
    private AmetisteRoutineCoreProperties props;

    @Autowired(required = false)
    private Map<String, OperationExecutorFactory> operationExecutorFactories = Collections.emptyMap();

    @Autowired(required = false)
    private Map<String, OperationExecutor> operationExecutors = Collections.emptyMap();

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskAppEvenets taskAppEvenets;

    @Autowired
    private ProtocolGatewayService protocolGatewayservice;

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

        return new DefaultOperationExecutionGateway(factories, protocolGatewayservice);
    }

    @Bean
    @ConditionalOnMissingBean
    // NOTE: DefaultTaskExecutionService implements ExecutionFeedback interface also, so we need it as type
    public DefaultTaskExecutionService taskExecutionService() {
        return new DefaultTaskExecutionService(taskRepository, taskAppEvenets, operationExecutionGateway());
    }

    @Bean
    @ConditionalOnMissingBean
    public JmsTaskEventsListener issuedTaskEventListener() {
        return new JmsTaskEventsListener(taskExecutionService(),
                taskExecutionService(),  // NOTE: DefaultTaskExecutionService implements ExecutionFeedback interface
                operationExecutionGateway(),
                props.getInitialExecutionConcurrency()
        );
    }

}
