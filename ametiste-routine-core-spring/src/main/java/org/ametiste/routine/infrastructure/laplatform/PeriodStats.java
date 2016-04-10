package org.ametiste.routine.infrastructure.laplatform;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @since
 */
class PeriodStats {

    private final static PeriodStats EMPTY = new PeriodStats(Duration.ZERO, Stats.empty(), Stats.empty());

    private final Duration duration;
    private final LocalDateTime renewAt;

    /**
     * Hold stats for the current runing period. Not aggregated.
     */
    private final Stats currentStats;

    /**
     * Hold stats for the pevious period. Aggregated.
     */
    private final Stats previousStats;

    PeriodStats(final Duration duration, final Stats currentStats, final Stats previousStats) {
        this(LocalDateTime.now().plus(duration), duration, currentStats, previousStats);
    }

    PeriodStats(final LocalDateTime renewAt, final Duration duration, final Stats currentStats) {
        this(renewAt, duration, currentStats, Stats.empty());
    }

    PeriodStats(final LocalDateTime renewAt,
                final Duration duration,
                final Stats currentStats,
                final Stats previousStats) {
        this.duration = duration;
        this.renewAt = renewAt;
        this.currentStats = currentStats;
        this.previousStats = previousStats;
    }


    public static final PeriodStats renewedAfter(final Duration duration) {
        return new PeriodStats(duration, Stats.empty(), Stats.empty());
    }

    public static final PeriodStats empty() {
        return EMPTY;
    }

    public Duration duration() {
        return duration;
    }

    public LocalDateTime renewAt() {
        return renewAt;
    }

    /**
     * Returns count of created protocols for previous aggregation period.
     *
     * @return count of created protocols
     */
    public long createdForPeriod() {
        return previousStats.createdCount();
    }

    PeriodStats incCreated() {
        return renewOrContinue(Stats::incCreated);
    }

    PeriodStats incCurrent() {
        return renewOrContinue(Stats::incCurrent);
    }

    /**
     * <p>
     *  Checks that the current renew period is done or note. Creates period with the new
     *  duration or continious previous period with the current stats transformed by the given
     *  transformation.
     * </p>
     *
     * <p>
     *  If the {@link #duration} is zero, this method doing notning and just return {@link #EMPTY}
     *  period stats, because no stats can't be gathered for the zero period in anyway.
     * </p>
     *
     * @param statsTransform transformation that would be applied to the current period stats
     *
     * @return renewed or continued period with stats modified by the given transformation, can't be null
     */
    private PeriodStats renewOrContinue(Function<Stats, Stats> statsTransform) {

        final PeriodStats result;

        if (duration.isZero()) {
            result = EMPTY;
        } else if (isMustBeRenewed()) {
            result = new PeriodStats(duration, statsTransform.apply(Stats.empty()), currentStats);
        } else {
            result = new PeriodStats(renewAt, duration, statsTransform.apply(currentStats), previousStats);
        }

        return result;

    }

    private boolean isMustBeRenewed() {
        return LocalDateTime.now().isAfter(renewAt);
    }

}
