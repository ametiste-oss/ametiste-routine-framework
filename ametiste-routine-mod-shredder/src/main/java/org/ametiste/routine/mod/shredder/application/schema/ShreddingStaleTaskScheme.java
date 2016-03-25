package org.ametiste.routine.mod.shredder.application.schema;

import org.ametiste.routine.domain.scheme.AbstractTaskScheme;
import org.ametiste.routine.domain.scheme.TaskOperationReceiver;
import org.ametiste.routine.mod.shredder.application.operation.ShreddingStaleTaskOperationScheme;
import org.ametiste.routine.mod.shredder.application.operation.ShreddingParams;
import org.ametiste.routine.mod.shredder.mod.ModShredder;
import org.springframework.stereotype.Component;

@Component
public class ShreddingStaleTaskScheme extends AbstractTaskScheme<ShreddingParams> {

    public static final String NAME = ModShredder.MOD_ID + "-scheme-shredding-stale-tasks";

    public ShreddingStaleTaskScheme() {
        super(NAME, ShreddingParams::new);
    }

    @Override
    protected void fulfillOperations(final TaskOperationReceiver task, final ShreddingParams schemeParams) {
        task.addOperation(ShreddingStaleTaskOperationScheme.class, schemeParams::proxy);
    }

    // TODO: add constraint to check scheme client, only mod-shredder is allowed to use this scheme to issue tasks

}