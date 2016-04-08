package org.ametiste.routine.mod.shredder.application.operation;

import org.ametiste.routine.domain.scheme.StatelessOperationScheme;
import org.ametiste.routine.mod.shredder.mod.ModShredder;
import org.springframework.stereotype.Component;

/**
 *
 * @since
 */
@Component
public class ShreddingStaleTaskOperationScheme extends StatelessOperationScheme<ShreddingParams> {

    public static final String NAME = ModShredder.MOD_ID + "-op-shredding-stale";

    public ShreddingStaleTaskOperationScheme() {
        super(NAME, DirectShreddingParams::new, ShreddingStaleTaskExecutor::new);
    }

}
