package org.ametiste.routine.application.action;

import org.ametiste.routine.configuration.AmetisteRoutineCoreProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @since
 */
@ConfigurationProperties(prefix = AmetisteRoutineCoreProperties.PREFIX_MOD + ".task-timeout")
public class TaskTimeoutActionProperties {

    private int defaultTimeout = 60;

    public void setDefaultTimeout(final int defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    public int getDefaultTimeout() {
        return defaultTimeout;
    }

}
