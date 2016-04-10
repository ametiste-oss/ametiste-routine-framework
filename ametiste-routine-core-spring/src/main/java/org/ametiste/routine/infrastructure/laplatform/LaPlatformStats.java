package org.ametiste.routine.infrastructure.laplatform;

import org.ametiste.laplatform.sdk.protocol.Protocol;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * <p>
 *     Note, this class is designed to be modified in a single thread.
 * </p>
 *
 * @since
 */
@Component
// TODO: move to @Configuration component
// TODO: move to lp project
// TODO: I guess ProtocolGatewayService may enclose this object and provide load method only, inc/dec methods
// may be internal for package
public class LaPlatformStats {

    final static Duration aggregateionPeriod = Duration.ofSeconds(30);

    final private ConcurrentHashMap<Class<? extends Protocol>, ProtocolStats> stats
            = new ConcurrentHashMap<>();

    public ProtocolStats loadProtocolStats(Class<? extends Protocol> protocolType) {
        return stats.getOrDefault(protocolType, ProtocolStats.empty());
    }

    public void incCreated(Class<? extends Protocol> protocolType) {
        stats.put(protocolType,
            stats.getOrDefault(protocolType, ProtocolStats.periodic(aggregateionPeriod)).incCreated()
        );
    }

    public void incCurrent(Class<? extends Protocol> protocolType) {
        stats.put(protocolType,
            stats.getOrDefault(protocolType, ProtocolStats.periodic(aggregateionPeriod)).incCurrent()
        );
    }

    public void decCurrent(Class<? extends Protocol> protocolType) {
        if (stats.containsKey(protocolType)) {
            stats.put(protocolType, stats.get(protocolType).decCurrent()
            );
        }
    }

}
