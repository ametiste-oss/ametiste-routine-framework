package org.ametiste.routine.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 *     Core properties, configures base features of the system.
 * </p>
 *
 * @since 0.1.0
 */
@ConfigurationProperties
public class AmetisteRoutineCoreProperties {

    /**
     * Defines how many workers would initialized to execute pending tasks.
     *
     * @sine 0.1.0
     */
    private int initialExecutionConcurrency = 5;

    public int getInitialExecutionConcurrency() {
        return initialExecutionConcurrency;
    }

    public void setInitialExecutionConcurrency(int initialExecutionConcurrency) {
        this.initialExecutionConcurrency = initialExecutionConcurrency;
    }
}
