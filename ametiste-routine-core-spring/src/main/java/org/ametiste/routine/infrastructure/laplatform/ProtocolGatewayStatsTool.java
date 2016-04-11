package org.ametiste.routine.infrastructure.laplatform;

import org.ametiste.laplatform.protocol.tools.ProtocolGatewayInstrumentary;
import org.ametiste.laplatform.protocol.tools.ProtocolGatewayTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @since
 */
// TODO: move to lp project
class ProtocolGatewayStatsTool implements ProtocolGatewayTool {

    private final ExecutorService executorService;
    private final LaPlatformStatsService laPlatformStatsService;

    public ProtocolGatewayStatsTool(final LaPlatformStatsService laPlatformStatsService) {
        this.laPlatformStatsService = laPlatformStatsService;
        // TODO: use some shared executor, if possible someone from GTE
        this.executorService = Executors.newFixedThreadPool(1);
    }

    @Override
    public void apply(final ProtocolGatewayInstrumentary gateway) {

        gateway.listenProtocolConnection(
            (protocolType, protocol, name, group) -> {
                executorService.execute(() -> {
                    laPlatformStatsService.incCreated(protocolType, name);
                    laPlatformStatsService.incCurrent(protocolType, name);
                });
            }
        );

        gateway.listenProtocolDisconnected(
            (protocolType, protocol, name, group) -> {
                executorService.execute(() -> {
                    laPlatformStatsService.decCurrent(protocolType);
                });
            }
        );

    }

    @PreDestroy
    public void finish() {
        executorService.shutdownNow();
    }

}
