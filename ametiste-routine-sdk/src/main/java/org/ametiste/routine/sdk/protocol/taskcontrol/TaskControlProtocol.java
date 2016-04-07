package org.ametiste.routine.sdk.protocol.taskcontrol;

import org.ametiste.laplatform.sdk.protocol.Protocol;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * <p>
 *     Defines protocol to control tasks termination lifecycle.
 * </p>
 *
 * <p>
 *     Note, clients of this protocol may affect termination of tasks, so
 *     if it is required, setup access grants for this protocol usage.
 * </p>
 *
 * @since 0.1.0
 */
public interface TaskControlProtocol extends Protocol {

    /**
     * <p>
     *      Terminates task if possible, in case where task already in
     *      terminated or done state doing nothing.
     * </p>
     *
     * <p>
     *      Note, if task termination failed the cause exeption may be acquired through
     *      {@code failure} consumer object.
     * </p>
     *
     * @param taskId identifier of task to be terminated, must be not null
     * @param message reason of termination, may be null
     * @param failure consumer of failure reasons, may be null
     */
    void terminateTask(UUID taskId, Optional<String> message,
                       Optional<Consumer<Exception>> failure);

    /**
     * <p>
     *      Terminates task if possible, in case where task already in
     *      terminated or done state doing nothing.
     * </p>
     *
     * @param taskId identifier of task to be terminated, must be not null
     * @param message reason of termination, may be null
     */
    default void terminateTask(UUID taskId, String message) {
        this.terminateTask(taskId, Optional.ofNullable(message), Optional.empty());
    };

    /**
     * <p>
     *      Terminates task if possible, in case where task already in
     *      terminated or done state doing nothing.
     * </p>
     *
     * @param taskId identifier of task to be terminated, must be not null
     * @param message reason of termination, may be null
     * @param failure consumer of failure reasons, may be null
     *
     */
    default void terminateTask(UUID taskId, String message, Consumer<Exception> failure) {
        this.terminateTask(taskId, Optional.ofNullable(message), Optional.ofNullable(failure));
    };

    /**
     * <p>
     *      Terminates task if possible, in case where task already in
     *      terminated or done state doing nothing.
     * </p>
     *
     * @param taskId identifier of task to be terminated, must be not null
     * @param failure consumer of failure reasons, may be null
     *
     */
    default void terminateTask(UUID taskId, Consumer<Exception> failure) {
        this.terminateTask(taskId, Optional.empty(), Optional.ofNullable(failure));
    };

    /**
     * <p>
     *      Terminates task if possible, in case where task already in
     *      terminated or done state doing nothing.
     * </p>
     *
     * @param taskId identifier of task to be terminated, must be not null
     */
    default void terminateTask(UUID taskId) {
        this.terminateTask(taskId, Optional.empty(), Optional.empty());
    };

}
