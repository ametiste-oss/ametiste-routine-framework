package org.ametiste.routine.infrastructure.laplatform;

/**
 *
 * @since
 */
// TODO: move to lp project
public class ProtocolStats {

    public final static ProtocolStats EMPTY_STATS = new ProtocolStats(0, 0);

    private final long createdCount;

    private final long currentCount;

    ProtocolStats(final long createdCount, final long currentCount) {
        this.createdCount = createdCount;
        this.currentCount = currentCount;
    }

    public long createdCount() {
        return createdCount;
    }

    public long currentCount() {
        return currentCount;
    }

    public ProtocolStats incrementCreated() {
        return new ProtocolStats(createdCount + 1, currentCount);
    }

    public ProtocolStats incrementCurrent() {
        return new ProtocolStats(createdCount, currentCount + 1);
    }

    public ProtocolStats decrementCurrent() {
        return new ProtocolStats(createdCount, currentCount - 1);
    }

    public static ProtocolStats empty() {
        return EMPTY_STATS;
    }
}
