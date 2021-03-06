package org.ametiste.routine.mod.tasklog.configuration;

import org.ametiste.laplatform.protocol.ProtocolFactory;
import org.ametiste.routine.mod.tasklog.domain.TaskLogRepository;
import org.ametiste.routine.mod.tasklog.infrastructure.persistency.jpa.JPATaskLogDataRepository;
import org.ametiste.routine.mod.tasklog.infrastructure.persistency.jpa.SpringDataTaskLogRepository;
import org.ametiste.routine.mod.tasklog.interfaces.web.TaskLogController;
import org.ametiste.routine.mod.tasklog.mod.DirectTaskLogConnection;
import org.ametiste.routine.mod.tasklog.mod.TaskLogGateway;
import org.ametiste.routine.mod.tasklog.mod.TaskLogProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @since
 */
@Configuration
@ComponentScan(basePackageClasses = TaskLogController.class)
public class RoutineTasklogModConfiguration {

    @Autowired
    private JPATaskLogDataRepository jpaTaskLogDataRepository;

    @Bean
    public TaskLogRepository taskLogRepository() {
        return new SpringDataTaskLogRepository(jpaTaskLogDataRepository);
    }

    @Bean
    public TaskLogGateway taskLogGateway() {
         return new TaskLogGateway();
    }

    // TODO: may be I should move it into ModGateway definition?
    @Bean
    public ProtocolFactory<TaskLogProtocol> modTaskLogProtocolFactory() {
         return (c) -> new DirectTaskLogConnection(jpaTaskLogDataRepository);
    }

}
