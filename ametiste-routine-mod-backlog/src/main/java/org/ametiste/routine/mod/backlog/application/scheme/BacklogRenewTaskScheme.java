package org.ametiste.routine.mod.backlog.application.scheme;

import org.ametiste.routine.domain.scheme.AbstractTaskScheme;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.mod.backlog.application.operation.BacklogRenewOperationExecutor;
import org.ametiste.routine.mod.backlog.mod.ModBacklog;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 * @since
 */
@Component(BacklogRenewTaskScheme.NAME)
public class BacklogRenewTaskScheme extends AbstractTaskScheme {

    public static final String NAME = ModBacklog.MOD_ID + "::renewTaskScheme";

    @Override
    protected void fulfillOperations(Task task, Map<String, String> schemeParams) {
        task.addOperation(BacklogRenewOperationExecutor.NAME, schemeParams);
    }

}
