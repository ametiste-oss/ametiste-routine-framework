package org.ametiste.routine.infrastructure.laplatform;

/**
 *
 * @since
 */
// TODO: move to lp project
public class Stats {

    public final static Stats EMPTY_STATS = new Stats(0, 0);

    private final long createdCount;
    private final long currentCount;

    Stats(final long createdCount, final long currentCount) {
        this.createdCount = createdCount;
        this.currentCount = currentCount;
    }

    public long createdCount() {
        return createdCount;
    }

    public long currentCount() {
        return currentCount;
    }

    Stats incCreated() {
        return new Stats(createdCount + 1, currentCount);
    }

    Stats incCurrent() {
        return new Stats(createdCount, currentCount + 1);
    }

    Stats decCurrent() {
        return new Stats(createdCount, currentCount - 1);
    }

    static Stats empty() {
        return EMPTY_STATS;
    }
}
