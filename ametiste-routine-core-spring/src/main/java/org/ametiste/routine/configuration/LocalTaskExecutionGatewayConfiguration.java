package org.ametiste.routine.configuration;

import org.ametiste.laplatform.protocol.gateway.ProtocolGatewayService;
import org.ametiste.metrics.MetricsService;
import org.ametiste.routine.application.CoreEventsGateway;
import org.ametiste.routine.application.TaskDomainEvenetsGateway;
import org.ametiste.routine.domain.scheme.SchemeRepository;
import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.infrastructure.execution.LineExecutionGateway;
import org.ametiste.routine.infrastructure.execution.OperationRuntimeProtocolFactory;
import org.ametiste.routine.infrastructure.execution.TaskExecutionGateway;
import org.ametiste.routine.infrastructure.execution.local.LocalLineExecutionGateway;
import org.ametiste.routine.infrastructure.execution.local.LocalTaskExecutionController;
import org.ametiste.routine.infrastructure.execution.local.LocalTaskExecutionGateway;
import org.ametiste.routine.infrastructure.execution.local.TaskExecutionController;
import org.ametiste.routine.infrastructure.messaging.JmsTaskExecutionGatewayListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 *
 * @since
 */
@Configuration
@EnableConfigurationProperties(AmetisteRoutineCoreProperties.class)
public class LocalTaskExecutionGatewayConfiguration {

    @Autowired
    private AmetisteRoutineCoreProperties props;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskDomainEvenetsGateway domainEvenetsGateway;

    @Autowired
    private CoreEventsGateway coreEventsGateway;

    @Autowired
    private ProtocolGatewayService protocolGatewayservice;

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    private MetricsService metricsService;

    @Autowired
    private List<OperationRuntimeProtocolFactory<?>> operationRuntimeProtocolFactories;

    @Bean
    public LineExecutionGateway localLineExecutionGateway() {

        // v1.1 UPDATE:
        //
        // Now OperationExecutorFactory is enclosed within the OperationScheme, but all semantics
        // described bellow still applicable for.
        //
        // DOCUMENTATE ME:
        //
        // OperationExecutorFactory allows to control process of OperationExecutor creation,
        // that may be useful when OperationExecutor is stateful or require additional configuration.
        // Also it may be used if new executor instance required for each operation termination.
        //
        // Registered OperationExecutor beans will be adopted to OperationExecutorFactory.
        //
        // Note, be careful to use OperationExecutor shortcut, be shure to use it only for stateless
        // executors.
        //

        //
        // NOTE: adoptation of stateless executors to factories, to shortcut configuration for
        // stateless executors, that anonymous factory will return the one instance of an executor for
        // each request.
        //

        return new LocalLineExecutionGateway(schemeRepository,
                localTaskExecutionController(), protocolGatewayservice, operationRuntimeProtocolFactories);
    }

    @Bean
    public TaskExecutionController localTaskExecutionController() {
        return new LocalTaskExecutionController(taskRepository, domainEvenetsGateway, coreEventsGateway);
    }

    @Bean
    public TaskExecutionGateway localTaskExecutionGateway() {
        return new LocalTaskExecutionGateway(
            localLineExecutionGateway(),
            props.getInitialExecutionConcurrency(),
            localTaskExecutionController()
        );
    }

    @Bean
    public JmsTaskExecutionGatewayListener jmsTaskExecutionGatewayListener() {
        return new JmsTaskExecutionGatewayListener(localTaskExecutionGateway());
    }

}
