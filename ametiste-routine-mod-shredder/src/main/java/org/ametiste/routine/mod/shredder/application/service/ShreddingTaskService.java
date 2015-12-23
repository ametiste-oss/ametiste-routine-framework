package org.ametiste.routine.mod.shredder.application.service;

import org.ametiste.laplatform.protocol.gateway.ProtocolGatewayService;
import org.ametiste.routine.mod.shredder.application.operation.ShreddingStaleTaskOperationExecutor;
import org.ametiste.routine.mod.shredder.application.schema.ShreddingStaleTaskScheme;
import org.ametiste.routine.mod.shredder.mod.ModShredder;
import org.ametiste.routine.sdk.protocol.taskpool.TaskPoolProtocol;

import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @since
 */
public class ShreddingTaskService {

    private final ProtocolGatewayService protocolGatewayService;
    private final String staleThresholdValue;
    private final String staleThresholdUnit;

    public ShreddingTaskService(
            ProtocolGatewayService protocolGatewayService,
            String staleThresholdValue,
            String staleThresholdUnit) {
        this.protocolGatewayService = protocolGatewayService;
        this.staleThresholdValue = staleThresholdValue;
        this.staleThresholdUnit = staleThresholdUnit;
    }

    public void issueShreddingTask() {

        final HashMap<String, String> p = new HashMap<>();
        p.put(ShreddingStaleTaskOperationExecutor.PARAM_STALE_THRESHOLD_VALUE, staleThresholdValue);
        p.put(ShreddingStaleTaskOperationExecutor.PARAM_STALE_THRESHOLD_UNIT, staleThresholdUnit);

        protocolGatewayService.createGateway(ModShredder.MOD_ID)
                .session(TaskPoolProtocol.class)
                .issueTask(ShreddingStaleTaskScheme.NAME, p);

    }

}
