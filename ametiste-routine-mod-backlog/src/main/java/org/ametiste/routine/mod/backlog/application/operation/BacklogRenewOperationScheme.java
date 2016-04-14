package org.ametiste.routine.mod.backlog.application.operation;

import org.ametiste.routine.domain.scheme.StatelessOperationScheme;
import org.ametiste.routine.mod.backlog.mod.ModBacklog;
import org.springframework.stereotype.Component;

@Component
public class BacklogRenewOperationScheme extends StatelessOperationScheme<BacklogParams> {

    public static final String NAME = ModBacklog.MOD_ID + "-renew-operation";

    public BacklogRenewOperationScheme() {
        super(NAME, DirectBacklogParams::new, BacklogRenewExecutor::new);
    }

}
