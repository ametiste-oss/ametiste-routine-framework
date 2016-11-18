package org.ametiste.routine.mod.shredder.configuration;

import org.ametiste.routine.mod.shredder.application.operation.DirectShreddingParams;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 *
 * <i>See <a href="https://github.com/ametiste-oss/ametiste-routine-framework/wiki/Action-:-Shredding">
 *     Routine Wiki : Action : Shredding</a> for configuration details</i>
 *
 * @since 0.2.0
 */
@ConfigurationProperties(prefix = ModShredderConfiguration.PREFIX)
public class ModShredderProperties {

    private StaleThreshold staleThreshold = new StaleThreshold();

    private List<String> staleStates = DirectShreddingParams.DEFAULT_STALE_STATES;

    private boolean disableSessionOptions = DirectShreddingParams.DEFAULT_DISABLE_SESSION_OPTIONS;

    public static class StaleThreshold {

        private int staleThresholdValue = DirectShreddingParams.DEFAULT_STALE_THRESHOLD_VALUE;

        private ChronoUnit staleThresholdUnit = DirectShreddingParams.DEFAULT_STALE_THRESHOLD_UNIT;

        public int getValue() {
            return staleThresholdValue;
        }

        public ChronoUnit getUnit() {
            return staleThresholdUnit;
        }

        public void setValue(final int staleThresholdValue) {
            this.staleThresholdValue = staleThresholdValue;
        }

        public void setUnit(final String staleThresholdUnit) {
            this.staleThresholdUnit = ChronoUnit.valueOf(staleThresholdUnit);
        }

    }

    public StaleThreshold getStaleThreshold() {
        return staleThreshold;
    }

    public void setStaleThreshold(final StaleThreshold staleThreshold) {
        this.staleThreshold = staleThreshold;
    }

    public List<String> getStaleStates() {
        return staleStates;
    }

    public void setStaleStates(final List<String> staleStates) {
        this.staleStates = staleStates;
    }

    public boolean isDisableSessionOptions() {
        return disableSessionOptions;
    }

    public void setDisableSessionOptions(boolean disableSessionOptions) {
        this.disableSessionOptions = disableSessionOptions;
    }
}
