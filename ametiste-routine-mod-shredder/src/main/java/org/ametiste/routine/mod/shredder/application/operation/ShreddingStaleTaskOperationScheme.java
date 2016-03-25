package org.ametiste.routine.mod.shredder.application.operation;

import org.ametiste.routine.domain.scheme.AbstractOperationScheme;
import org.ametiste.routine.domain.scheme.OperationScheme;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.mod.shredder.mod.ModShredder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 * @since
 */
@Component
public class ShreddingStaleTaskOperationScheme extends AbstractOperationScheme<ShreddingParams> {

    public static final String NAME = ModShredder.MOD_ID + "-op-shredding-stale";

    public ShreddingStaleTaskOperationScheme() {
        super(NAME, ShreddingParams::new);
    }

}
