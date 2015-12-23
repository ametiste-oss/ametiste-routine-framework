package org.ametiste.routine.mod.shredder.application.schema;

import org.ametiste.routine.domain.scheme.AbstractTaskScheme;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.mod.shredder.application.operation.ShreddingStaleTaskOperationExecutor;
import org.ametiste.routine.mod.shredder.mod.ModShredder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component(ShreddingStaleTaskScheme.NAME)
public class ShreddingStaleTaskScheme extends AbstractTaskScheme {

    public static final String NAME = ModShredder.MOD_ID + "::shreddingStaleTaskScheme";

    @Override
    protected void fulfillOperations(Task task, Map<String, String> schemeParams) {
        task.addOperation(ShreddingStaleTaskOperationExecutor.NAME, schemeParams);
    }

}