package org.ametiste.routine.mod.backlog.application.scheme;

import org.ametiste.routine.domain.scheme.AbstractTaskScheme;
import org.ametiste.routine.domain.scheme.TaskOperationReceiver;
import org.ametiste.routine.mod.backlog.application.operation.BacklogParams;
import org.ametiste.routine.mod.backlog.application.operation.BacklogRenewOperationScheme;
import org.ametiste.routine.mod.backlog.application.operation.DirectBacklogParams;
import org.ametiste.routine.mod.backlog.mod.ModBacklog;
import org.springframework.stereotype.Component;

@Component
public class BacklogRenewTaskScheme extends AbstractTaskScheme<BacklogParams> {

    public static final String NAME = ModBacklog.MOD_ID + "::renewTaskScheme";

    public BacklogRenewTaskScheme() {
        super(NAME, DirectBacklogParams::new);
    }

    @Override
    protected void fulfillOperations(final TaskOperationReceiver task, final BacklogParams schemeParams) {
        task.addOperation(BacklogRenewOperationScheme.class, schemeParams::proxy);
    }
}
