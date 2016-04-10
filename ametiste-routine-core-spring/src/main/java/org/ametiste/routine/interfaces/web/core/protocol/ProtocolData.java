package org.ametiste.routine.interfaces.web.core.protocol;

import org.ametiste.laplatform.sdk.protocol.Protocol;
import org.ametiste.laplatform.sdk.protocol.ProtocolFactory;
import org.ametiste.routine.infrastructure.laplatform.ProtocolStats;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @since
 */
public class ProtocolData {

    private final String name;
    private final Class<? extends Protocol> type;
    private final ProtocolFactory<? extends Protocol> factory;
    private final Map<String, String> operationsMapping;
    private final boolean isProduceEvents;
    private final ProtocolStats protocolStats;

    public ProtocolData(final String name,
                        final Class<? extends Protocol> type,
                        final ProtocolFactory<? extends Protocol> factory,
                        final Map<String, String> operationsMapping,
                        final boolean isProduceEvents,
                        final ProtocolStats protocolStats) {
        this.name = name;
        this.type = type;
        this.factory = factory;
        this.operationsMapping = operationsMapping;
        this.isProduceEvents = isProduceEvents;
        this.protocolStats = protocolStats;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type.getName();
    }

    public String getFactory() {
        return factory.getClass().getName();
    }

    public Map<String, String> getOperationsMapping() {
        return operationsMapping;
    }

    public boolean isProduceEvents() {
        return isProduceEvents;
    }

    public Map<String, Object> getProtocolStats() {
        final HashMap<String, Object> stats = new HashMap<>();
        stats.put("total-created", protocolStats.createdCount());
        stats.put("current-runing", protocolStats.currentCount());
        stats.put("period", protocolStats.period().toString());
        stats.put("renewed-at", protocolStats.renewAt().toString());
        stats.put("total-for-period", protocolStats.createdForPeriod());
        return stats;
    }
}
