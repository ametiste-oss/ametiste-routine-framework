package org.ametiste.routine.dsl.infrastructure;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.laplatform.protocol.tools.ProtocolGatewayInstrumentary;
import org.ametiste.laplatform.protocol.tools.ProtocolGatewayTool;
import org.ametiste.metrics.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Implements dynamic protocols metrics.
 * </p>
 *
 * <p>
 *  This tool is listen <i>timing</i> and <i>exception</i> events produced by a {@link ProtocolGateway},
 *  for each of these events the tool provides particular metric event.
 * </p>
 *
 * <p>
 *  Note, at the moment this tool configured as {@link Component} to shortcut configuration effort,
 *  but it will be changed in a one of next versions. Next after suitable configuration
 *  layout definition will be done.
 * </p>
 *
 * @since 1.1
 */
@Component
// TODO: remove @Component and move configuration to suitable @Configuration component
class DynamicProtocolMetricsTool implements ProtocolGatewayTool {

    private final MetricsService metricsService;

    @Autowired
    public DynamicProtocolMetricsTool(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @Override
    public void apply(final ProtocolGatewayInstrumentary gateway) {

        gateway.listenInvocationsTiming((c, p, g, o, t) -> {
            metricsService.createEvent(g + "." + p + ".overall." + o + ".timing", (int) t);
            metricsService.createEvent(g + "." + p + ".clients." + c + "." + o + ".timing", (int) t);
        });

        gateway.listenErrors((c, p, g, o, e) -> {
            metricsService.createEvent(g + "." + p + ".overall." + o + ".errors", 1);
            metricsService.createEvent(g + "." + p + ".clients." + c + "." + o + ".errors", 1);
        });

    }
}
