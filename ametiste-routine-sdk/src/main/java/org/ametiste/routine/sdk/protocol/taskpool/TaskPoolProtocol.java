package org.ametiste.routine.sdk.protocol.taskpool;

import org.ametiste.laplatform.protocol.Protocol;
import org.ametiste.routine.sdk.domain.TaskFilter;

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
 *     Note, some protocol opertions may have distructive effect for entire application lifecicle,
 *     so if some mods are not trusted manage their access to this protocol.
 * </p>
 *
 * @since 0.1.0
 */
public interface TaskPoolProtocol extends Protocol {

    UUID issueTask(String taskScheme, Map<String, String> params);

    /**
     * <p>
     *      Removes tasks that match the given state and completion time less than given.
     *
     *      @return count of removed tasks
     * </p>
     */
    long removeTasks(List<String> states, Instant afterDate);

}
