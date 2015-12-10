package org.ametiste.routine;

import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.ametiste.routine.sdk.operation.OperationExecutorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
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

        checkExecutors(context);
    }

    private static void checkExecutors(ConfigurableApplicationContext context) {
        final Map<String, Object> executors = new HashMap<>();

        executors.putAll(
                context.getBeansOfType(OperationExecutorFactory.class));

        executors.putAll(
                context.getBeansOfType(OperationExecutor.class));


        if (executors.size() == 0) {
            log.warn("!!! Configuration Warning !!!");
            log.warn("No registered OperationExecutor/Factory instances found, please check your classpath for bundles.");
            log.warn(" ---    --------------    ---");
        } else {
            log.info("### Configured OperationExecutor/Factory instances ###");
            executors.forEach(
                    (k,v) -> log.info("-- " + k + " [" + v.getClass().getName() + "]")
            );
        }
    }
}
