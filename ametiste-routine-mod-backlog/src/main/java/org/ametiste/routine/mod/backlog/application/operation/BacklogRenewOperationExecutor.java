package org.ametiste.routine.mod.backlog.application.operation;

import org.ametiste.routine.mod.backlog.domain.Backlog;
import org.ametiste.routine.mod.backlog.domain.BacklogRepository;
import org.ametiste.routine.mod.backlog.domain.RenewScheme;
import org.ametiste.routine.mod.backlog.domain.RenewSchemeExecutor;
import org.ametiste.routine.mod.backlog.mod.ModBacklog;
import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.ametiste.routine.sdk.operation.OperationFeedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @since
 */
public class BacklogRenewOperationExecutor implements OperationExecutor {

    public static final String NAME = ModBacklog.MOD_ID + "::renewOperation";

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private RenewSchemeExecutor renewSchemeExecutor;

    @Override
    public void execOperation(UUID operationId, Map<String, String> properties, OperationFeedback feedback) {

        final Backlog backlog = backlogRepository.loadBacklogOf(properties.get("schemeName"));
        final RenewScheme renewScheme = backlog.createRenewScheme();

        backlogRepository.save(backlog);
        renewSchemeExecutor.executeRenewScheme(renewScheme);
    }

}
