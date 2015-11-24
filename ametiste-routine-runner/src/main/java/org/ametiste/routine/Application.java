package org.ametiste.routine;

import org.ametiste.routine.sdk.application.service.execution.OperationExecutorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

/**
 *
 * @since
 */
@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        final ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        final Map<String, OperationExecutorFactory> factories =
                context.getBeansOfType(OperationExecutorFactory.class);

        if (factories.size() == 0) {
            log.warn("!!! Configuration Warning !!!");
            log.warn("No registered OperationExecutorFactory found, please check your classpath for bundles.");
            log.warn(" ---    --------------    ---");
        } else {
            log.info("### Configured OperationExecutorFactory instances ###");
            factories.forEach(
                    (k,v) -> log.info("-- " + k + " [" + v.getClass().getName() + "]")
            );
        }

    }
}
