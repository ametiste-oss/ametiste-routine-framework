package org.ametiste.routine.infrastructure.protocol.taskpool;

import org.ametiste.laplatform.protocol.Protocol;
import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.sdk.protocol.operation.ParamsProtocol;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * <p>
 *     Protocol that allow to manage task pool. Task pool is abstract entity
 *     that represent all tasks currently registered on this {@code Routine} instance.
 * </p>
 *
 * <p>
 *     Note, some protocol opertions may have distructive effect for entire application lifecycle,
 *     so if there is some mods in environment, that are not trusted,
 *     manage their access level to this protocol sessions.
 * </p>
 *
 * @since 0.1.0
 */

// TODO: все таки хотелось бы иметь этот протокол только в сдк, но похоже что это не реально
// TODO: возможно, это реально протокол ядра и должен он находится только здесь
// TODO: но как тогда разделить пакеты сдк и ядра? вообще нужно подумать над нормальным разделением ядро / сдк
public interface TaskPoolProtocol extends Protocol {

    <T extends ParamsProtocol> UUID issueTask(Class<? extends TaskScheme<T>> taskScheme, Consumer<T> paramsInstaller);

    /**
     * <p>
     *      Removes tasks that match the given state and completion time less than given.
     *
     *      @return count of removed tasks
     * </p>
     */
    long removeTasks(List<String> states, Instant afterDate);

}
