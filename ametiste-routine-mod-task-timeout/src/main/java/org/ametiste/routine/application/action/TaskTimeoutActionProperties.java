package org.ametiste.routine.application.action;

import org.ametiste.routine.RoutineCoreSpring;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @since
 */
@ConfigurationProperties(prefix = RoutineCoreSpring.MOD_PROPS_PREFIX + ".task-timeout")
public class TaskTimeoutActionProperties {

    private int defaultTimeout = 60;

    public void setDefaultTimeout(final int defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    public int getDefaultTimeout() {
        return defaultTimeout;
    }

}
