package org.ametiste.routine.mod.executing.spring.boot.configuration;

import org.ametiste.gte.Action;
import org.ametiste.gte.ActionActuatorDelegate;
import org.ametiste.gte.ActionFactory;
import org.ametiste.routine.application.service.execution.DefaultTaskExecutionService;
import org.ametiste.routine.infrastructure.execution.DefaultOperationExecutionGateway;
import org.ametiste.routine.application.service.execution.TaskExecutionService;
import org.ametiste.routine.domain.log.TaskLogRepository;
import org.ametiste.routine.domain.task.TaskRepository;
import org.ametiste.routine.mod.executing.application.ExecuteTaskAction;
import org.ametiste.routine.sdk.application.service.execution.OperationExecutionGateway;
import org.ametiste.routine.sdk.application.service.execution.OperationExecutorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 *
 * @since
 */
@Configuration
@ConditionalOnProperty(prefix = "ame.routine.actions.execute-task", name = "enabled", matchIfMissing = true)
public class ExecuteTaskActionConfiguration {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskLogRepository taskLogRepository;

    @Autowired
    private Map<String, OperationExecutorFactory> operationExecutors;

    @Autowired
    private TaskExecutionService taskExecutionService;

    @Bean
    public ActionFactory executeTaskAction() {
        return new ActionFactory() {

            @Override
            public Action createAction(ActionActuatorDelegate actionActuatorDelegate) {

                // TODO: property for appendCount
                ExecuteTaskAction action =
                        new ExecuteTaskAction(taskExecutionService, taskLogRepository, 10);

                return action::executeAction;
            }

            @Override
            public String registrationName() {
                return "routine:executeTaskAction";
            }
        };
    }

}
