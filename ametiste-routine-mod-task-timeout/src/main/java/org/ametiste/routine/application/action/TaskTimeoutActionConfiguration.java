package org.ametiste.routine.application.action;

import org.ametiste.gte.Action;
import org.ametiste.gte.ActionActuatorDelegate;
import org.ametiste.gte.ActionFactory;
import org.ametiste.laplatform.protocol.gateway.ProtocolGatewayService;
import org.ametiste.routine.configuration.AmetisteRoutineCoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @since
 */
@Configuration
@ConditionalOnProperty(prefix = AmetisteRoutineCoreProperties.PREFIX,
        name = "action.task-timeout.enabled", matchIfMissing = false)
public class TaskTimeoutActionConfiguration {

    public static final String TIMEOUT_ACTION_ID = "mod-task-timeout:timeout-action";

    @Autowired
    private ProtocolGatewayService protocolGatewayService;

    @Bean
    public TaskTimeoutAction taskTimeoutAction() {
        return new TaskTimeoutAction(1);
    }

    @Bean
    public ActionFactory taskTimeoutActionFactory() {
         return new ActionFactory() {

             // TODO: I need the way to redefine GTE execution service, need to have ability to
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
