package org.ametiste.routine.infrastructure.laplatform;

import org.ametiste.laplatform.sdk.protocol.Protocol;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * <p>
 *     Collects various {@code lambda-platform} stats.
 * </p>
 *
 * <p>
 *     Note, this class is designed to be modified in a single thread.
 * </p>
 *
 * @since
 */
// TODO: move to lp project
// TODO: I guess ProtocolGatewayService may enclose this object and provide load method only, inc/dec methods
// may be internal for package
public class LaPlatformStatsService {

    // TODO: configuration property required
    final static Duration aggregateionPeriod = Duration.ofSeconds(30);

    final private ConcurrentHashMap<Class<? extends Protocol>, ProtocolStats> stats
            = new ConcurrentHashMap<>();

    public void loadProtocolStats(Consumer<ProtocolStats> protocolStatsConsumer) {
        stats.values().stream()
                .filter(ProtocolStats::isNotEmpty)
                .forEach(protocolStatsConsumer);
    }

    public ProtocolStats loadProtocolStats(Class<? extends Protocol> protocolType) {
        return stats.getOrDefault(protocolType, ProtocolStats.empty());
    }

    void incCreated(Class<? extends Protocol> protocolType, final String name) {
        stats.put(protocolType,
            stats.getOrDefault(protocolType, ProtocolStats.periodic(name, aggregateionPeriod)).incCreated()
        );
    }

    void incCurrent(Class<? extends Protocol> protocolType, final String name) {
        stats.put(protocolType,
            stats.getOrDefault(protocolType, ProtocolStats.periodic(name, aggregateionPeriod)).incCurrent()
        );
    }

    void decCurrent(Class<? extends Protocol> protocolType) {
        if (stats.containsKey(protocolType)) {
            stats.put(protocolType, stats.get(protocolType).decCurrent());
        }
    }

}
