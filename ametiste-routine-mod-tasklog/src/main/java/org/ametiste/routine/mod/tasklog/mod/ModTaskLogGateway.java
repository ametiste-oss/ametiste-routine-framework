package org.ametiste.routine.mod.tasklog.mod;

import org.ametiste.routine.mod.tasklog.infrastructure.persistency.jpa.JPATaskLogDataRepository;
import org.ametiste.routine.sdk.mod.ModGateway;
import org.ametiste.routine.sdk.mod.ModInfoConsumer;

import java.util.Collections;

/**
 *
 * @since
 */
public class ModTaskLogGateway implements ModGateway {

    private final JPATaskLogDataRepository usedRepository;

    public ModTaskLogGateway(JPATaskLogDataRepository usedRepository) {
        this.usedRepository = usedRepository;
    }

    @Override
    public void provideModInfo(ModInfoConsumer modInfoConsumer) {
        modInfoConsumer.modInfo("mod-tasklog", "1.1",
            Collections.singletonMap("task.repository.used", usedRepository.getClass().getName()),
            Collections.emptyList()
        );
    }

}
