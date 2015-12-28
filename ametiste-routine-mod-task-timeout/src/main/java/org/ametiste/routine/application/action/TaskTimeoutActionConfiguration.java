package org.ametiste.routine.application.action;

import org.ametiste.gte.Action;
import org.ametiste.gte.ActionActuatorDelegate;
import org.ametiste.gte.ActionFactory;
import org.ametiste.laplatform.protocol.gateway.ProtocolGatewayService;
import org.ametiste.routine.configuration.AmetisteRoutineCoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @since
 */
@Configuration
@ConditionalOnProperty(prefix = AmetisteRoutineCoreProperties.PREFIX_MOD,
        name = "task-timeout.enabled", matchIfMissing = false)
@EnableConfigurationProperties(TaskTimeoutActionProperties.class)
public class TaskTimeoutActionConfiguration {

    public static final String TIMEOUT_ACTION_ID = "mod-task-timeout-action-timeout";

    @Autowired
    private TaskTimeoutActionProperties props;

    @Autowired
    private ProtocolGatewayService protocolGatewayService;

    @Bean
    public TaskTimeoutAction taskTimeoutAction() {
        return new TaskTimeoutAction(props.getDefaultTimeout());
    }

    @Bean
    public ActionFactory taskTimeoutActionFactory() {
         return new ActionFactory() {

             // TODO: I need the way to redefine GTE termination service, need to have ability to
             // pass some kind of context to actions
             @Override
             public Action createAction(ActionActuatorDelegate actionActuatorDelegate) {
                 return () -> {
                    taskTimeoutAction().executeAction(
                        protocolGatewayService.createGateway(TIMEOUT_ACTION_ID)
                    );
                 };
             }

             @Override
             public String registrationName() {
                 return TIMEOUT_ACTION_ID;
             }
         };
    }

}
