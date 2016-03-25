package org.ametiste.routine.mod.backlog.application.scheme;

import javafx.concurrent.Task;
import org.ametiste.routine.domain.scheme.AbstractTaskScheme;
import org.ametiste.routine.domain.scheme.TaskOperationInstaller;
import org.ametiste.routine.mod.backlog.application.operation.BacklogParams;
import org.ametiste.routine.mod.backlog.application.operation.BacklogRenewOperationScheme;
import org.ametiste.routine.mod.backlog.mod.ModBacklog;
import org.springframework.stereotype.Component;

@Component
public class BacklogRenewTaskScheme extends AbstractTaskScheme<BacklogParams> {

    public static final String NAME = ModBacklog.MOD_ID + "::renewTaskScheme";

    public BacklogRenewTaskScheme() {
        super(NAME, BacklogParams::new);
    }

    @Override
    // TODO: may I have shortuct for these cases, where scheme params just proxied to the one operation?
    protected void fulfillOperations(final TaskOperationInstaller task, final BacklogParams schemeParams) {
        task.addOperation(BacklogRenewOperationScheme.class, p -> p.schemeName(schemeParams.schemeName()));
    }
}
