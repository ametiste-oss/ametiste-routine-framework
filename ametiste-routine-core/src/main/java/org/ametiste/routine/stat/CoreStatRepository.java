package org.ametiste.routine.stat;

import java.util.Map;

/**
 *
 * @since 0.3.0
 */
public interface CoreStatRepository {

    void incrementStat(final String statName);

    void incrementStat(final String statName, final long taskCountToRemove);

    Long loadStat(String statName);

    Map<String, Long> loadStat();
}
