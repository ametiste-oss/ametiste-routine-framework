package org.ametiste.routine.infrastructure.laplatform;

import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 *
 * @since
 */
public class ProtocolStats {

    private final static ProtocolStats EMPTY = new ProtocolStats("", Stats.empty(), PeriodStats.empty());

    private final String name;
    private final Stats stats;
    private final PeriodStats periodStats;

    public ProtocolStats(final String name, final Stats stats, final PeriodStats periodStats) {
        this.name = name;
        this.stats = stats;
        this.periodStats = periodStats;
    }

    public String name() {
        return name;
    }

    public Duration period() {
        return periodStats.duration();
    }

    public long createdForPeriod() {
        return periodStats.createdForPeriod();
    }

    public long createdCount() {
        return stats.createdCount();
    }

    public long currentCount() {
        return stats.currentCount();
    }

    public LocalDateTime renewAt() {
        return periodStats.renewAt();
    }

    ProtocolStats incCreated() {
        return new ProtocolStats(name, stats.incCreated(), periodStats.incCreated());
    }

    ProtocolStats incCurrent() {
        return new ProtocolStats(name, stats.incCurrent(), periodStats.incCurrent());
    }

    ProtocolStats decCurrent() {
        return new ProtocolStats(name, stats.decCurrent(), periodStats);
    }

    static ProtocolStats periodic(final String name, Duration duration) {
        return new ProtocolStats(name, Stats.empty(), PeriodStats.renewedAfter(duration));
    }

    static ProtocolStats empty() {
        return EMPTY;
    }

    public boolean isNotEmpty() {
        return this != empty();
    }
}
