package org.ametiste.routine.mod.shredder.application.service;

import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.laplatform.protocol.gateway.ProtocolGatewayService;
import org.ametiste.routine.mod.shredder.application.operation.ShreddingParams;
import org.ametiste.routine.mod.shredder.application.schema.ShreddingStaleTaskScheme;
import org.ametiste.routine.mod.shredder.mod.ModShredder;
import org.ametiste.routine.infrastructure.protocol.taskpool.TaskPoolProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @since
 */
public class ShreddingTaskService {

    private final ProtocolGatewayService protocolGatewayService;
    private final List<String> staleStates;
    private final int staleThresholdValue;
    private final ChronoUnit staleThresholdUnit;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ShreddingTaskService(
            ProtocolGatewayService protocolGatewayService,
            List<String> staleStates,
            int staleThresholdValue,
            ChronoUnit staleThresholdUnit) {
        this.protocolGatewayService = protocolGatewayService;
        this.staleStates = staleStates;
        this.staleThresholdValue = staleThresholdValue;
        this.staleThresholdUnit = staleThresholdUnit;
    }

    public void issueShreddingTask() {

        if (logger.isDebugEnabled()) {
            logger.debug("Issue shredding tasks with staleStates: {}, threshold: {} {}", staleStates,
                    staleThresholdValue, staleThresholdUnit);
        }

        final ProtocolGateway gateway = protocolGatewayService
                .createGateway(ModShredder.MOD_ID, Collections.emptyMap());

        try {
            gateway.session(TaskPoolProtocol.class)
                    .issueTask(ShreddingStaleTaskScheme.class, p -> {
                        p.staleStates(staleStates);
                        p.staleThresholdValue(staleThresholdValue);
                        p.staleThresholdUnit(staleThresholdUnit);
                    });
        } finally {
            gateway.release();
        }

    }

}
