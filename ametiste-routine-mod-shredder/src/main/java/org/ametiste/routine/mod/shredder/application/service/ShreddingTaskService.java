package org.ametiste.routine.mod.shredder.application.service;

import org.ametiste.laplatform.protocol.gateway.ProtocolGatewayService;
import org.ametiste.routine.mod.shredder.application.operation.ShreddingStaleTaskOperationExecutor;
import org.ametiste.routine.mod.shredder.application.schema.ShreddingStaleTaskScheme;
import org.ametiste.routine.mod.shredder.mod.ModShredder;
import org.ametiste.routine.sdk.protocol.taskpool.TaskPoolProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @since
 */
public class ShreddingTaskService {

    private final ProtocolGatewayService protocolGatewayService;
    private final List<String> staleStates;
    private final int staleThresholdValue;
    private final String staleThresholdUnit;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ShreddingTaskService(
            ProtocolGatewayService protocolGatewayService,
            List<String> staleStates,
            int staleThresholdValue,
            String staleThresholdUnit) {
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

        final HashMap<String, String> p = new HashMap<>();
        p.put(ShreddingStaleTaskOperationExecutor.PARAM_STALE_THRESHOLD_VALUE, Integer.toString(staleThresholdValue));
        p.put(ShreddingStaleTaskOperationExecutor.PARAM_STALE_THRESHOLD_UNIT, staleThresholdUnit);
        p.put(ShreddingStaleTaskOperationExecutor.PARAM_STALE_STATES, String.join(",", staleStates));

        protocolGatewayService.createGateway(ModShredder.MOD_ID)
                .session(TaskPoolProtocol.class)
                .issueTask(ShreddingStaleTaskScheme.NAME, p);

    }

}
