package org.ametiste.routine.mod.shredder.configuration;

import org.ametiste.routine.mod.shredder.application.operation.ShreddingParams;
import org.springframework.boot.context.properties.ConfigurationProperties;

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

    private List<String> staleStates = ShreddingParams.DEFAULT_STALE_STATES;

    public static class StaleThreshold {

        private int staleThresholdValue = ShreddingParams.DEFAULT_STALE_THRESHOLD_VALUE;

        private String staleThresholdUnit = ShreddingParams.PARAM_STALE_THRESHOLD_UNIT;

        public int getValue() {
            return staleThresholdValue;
        }

        public String getUnit() {
            return staleThresholdUnit;
        }

        public void setValue(final int staleThresholdValue) {
            this.staleThresholdValue = staleThresholdValue;
        }

        public void setUnit(final String staleThresholdUnit) {
            this.staleThresholdUnit = staleThresholdUnit;
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
}
