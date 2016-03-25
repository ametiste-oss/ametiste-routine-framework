package org.ametiste.routine.mod.backlog.application.operation;

import org.ametiste.routine.domain.scheme.AbstractOperationScheme;
import org.ametiste.routine.domain.scheme.OperationScheme;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.mod.backlog.mod.ModBacklog;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 * @since
 */
@Component
public class BacklogRenewOperationScheme extends AbstractOperationScheme<BacklogParams> {

    public static final String NAME = ModBacklog.MOD_ID + "::renewOperation";

    public BacklogRenewOperationScheme() {
        super(NAME, BacklogParams::new);
    }

}
