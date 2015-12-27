package org.ametiste.routine.stat.persistency;

import org.ametiste.routine.stat.CoreStatRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 *
 * @since
 */
public class InMemoryCoreStatRepository implements CoreStatRepository {

    public Map<String, AtomicLong> counters = new HashMap<>();

    @Override
    public void incrementStat(String statName) {
        incrementStat(statName, 1L);
    }

    @Override
    public void incrementStat(final String statName, final long delta) {
        getCounter(statName).addAndGet(delta);
    }

    @Override
    public Long loadStat(final String statName) {
        return getCounter(statName).get();
    }

    @Override
    public Map<String, Long> loadStat() {
        return counters.entrySet().stream().collect(
                Collectors.toMap(k -> k.getKey(), v -> v.getValue().get())
        );
    }

    private AtomicLong getCounter(String statName) {
        return counters.computeIfAbsent(statName, k -> new AtomicLong());
    }

}
