package org.ametiste.routine.infrastructure.laplatform;

import jdk.nashorn.internal.objects.NativeArray;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.ametiste.routine.infrastructure.laplatform.Stats.EMPTY_STATS;

/**
 *
 * @since
 */
public class ProtocolStats {

    private final static ProtocolStats EMPTY = new ProtocolStats(Stats.empty(), PeriodStats.empty());

    private final Stats stats;
    private final PeriodStats periodStats;

    public ProtocolStats(final Stats stats, final PeriodStats periodStats) {
        this.stats = stats;
        this.periodStats = periodStats;
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
        return new ProtocolStats(stats.incCreated(), periodStats.incCreated());
    }

    ProtocolStats incCurrent() {
        return new ProtocolStats(stats.incCurrent(), periodStats.incCurrent());
    }

    ProtocolStats decCurrent() {
        return new ProtocolStats(stats.decCurrent(), periodStats);
    }

    static ProtocolStats periodic(Duration duration) {
        return new ProtocolStats(Stats.empty(), PeriodStats.renewedAfter(duration));
    }

    static ProtocolStats empty() {
        return EMPTY;
    }

}
