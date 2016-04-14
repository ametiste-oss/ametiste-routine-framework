package org.ametiste.routine.domain.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Object describes set of events that produced during {@link Task} aggregate state transition.
 * External clients may consume any of produced events.
 *
 * <p>
 * Note, this object is keep the order in which events are produced, so each event will be consumed in order
 * of events was producing, order of events consumer registration does not matter.
 * </p>
 *
 * @since 1.1
 */
public class TaskEvents {

    private final List<Consumer<TaskEvents>> consumeOrder;

    private final List<Consumer<TaskDoneEvent>> doneConsumers;
    private final List<Consumer<TaskTerminatedEvent>> terminatedConsumers;
    private final List<Consumer<OperationTerminatedEvent>> opTerminatedConsumers;

    /**
     * Construct new {@link TaskEvents} object, that contains consume
     * operation in the given order.
     *
     * <p>
     *     <b>Note</b>, the consume operations order is must be same with the order in which events was
     *     produced.
     * </p>
     *
     * @param consumeOrder ordered list of consume operations, can't be null.
     */
    private TaskEvents(List<Consumer<TaskEvents>> consumeOrder) {
        this.consumeOrder = new ArrayList<>(consumeOrder);
        this.doneConsumers = new ArrayList<>();
        this.terminatedConsumers = new ArrayList<>();
        this.opTerminatedConsumers = new ArrayList<>();
    }

    /**
     * Registers {@link TaskDoneEvent} consumer.
     *
     * @param consumer event consumer, must be not null
     * @return this {@link TaskEvents} object, can't be null
     * @throws IllegalArgumentException in case where consumer is null
     */
    public TaskEvents taskDone(Consumer<TaskDoneEvent> consumer) {
        doneConsumers.add(notNullConsumer(consumer));
        return this;
    }

    /**
     * Registers {@link TaskTerminatedEvent} consumer.
     *
     * @param consumer event consumer, must be not null
     * @return this {@link TaskEvents} object, can't be null
     * @throws IllegalArgumentException in case where consumer is null
     */
    public TaskEvents taskTerminated(Consumer<TaskTerminatedEvent> consumer) {
        terminatedConsumers.add(notNullConsumer(consumer));
        return this;
    }

    /**
     * Registers {@link OperationTerminatedEvent} consumer.
     *
     * @param consumer event consumer, must be not null
     * @return this {@link TaskEvents} object, can't be null
     * @throws IllegalArgumentException in case where consumer is null
     */
    public TaskEvents operationTerminated(Consumer<OperationTerminatedEvent> consumer) {
        opTerminatedConsumers.add(notNullConsumer(consumer));
        return this;
    }

    /**
     * Consumes events by each registered consumer.
     *
     * <p>
     * Events will be consumed in order of events produced by a {@link Task},
     * not in order in which consumers was registered.
     * </p>
     */
    public void consume() {
        consumeOrder.forEach(c -> c.accept(this));
    }

    private static final <T> Consumer<T> notNullConsumer(Consumer<T> consumer) {
        if (consumer == null) {
            throw new IllegalArgumentException("Events consumer can't be null.");
        }
        return consumer;
    }

    /**
     *  Internal builder object to construct {@link TaskEvents} that keeps order of events.
     *
     *  <p>
     *      During construction this builder creates ordered list of consumers that
     *      then passed as {@code TaskEvents} argument, this list contains operations to
     *      provide concrete events to consumers and is iterated by {@link TaskEvents#consume()},
     *      so that during a consume stage, consummers will receive
     *      events in the order in which these events was produced by the {@link Task}. But
     *      a client has ability to subscribe concrete consumer in a free order.
     *  </p>
     */
    static final class Builder {

        private List<Consumer<TaskEvents>> events = new ArrayList<>();

        private Builder() { }

        final void done(UUID taskId) {
            final TaskDoneEvent event = new TaskDoneEvent(taskId);
            events.add((e) -> e.doneConsumers.forEach(c -> c.accept(event)));
        }

        final void terminated(UUID taskId, List<UUID> terminatedOpIds, String cause) {
            final TaskTerminatedEvent event = new TaskTerminatedEvent(taskId, terminatedOpIds, cause);
            events.add((e) -> e.terminatedConsumers.forEach(c -> c.accept(event)));
        }

        final void operationTerminated(UUID taskId, UUID operationId) {
            final OperationTerminatedEvent event = new OperationTerminatedEvent(taskId, operationId);
            events.add((e) -> e.opTerminatedConsumers.forEach(c -> c.accept(event)));
        }

        private TaskEvents build() {
            return new TaskEvents(events);
        }

    }

    static TaskEvents empty() {
        return new TaskEvents(Collections.emptyList());
    }

    static final TaskEvents build(final Consumer<Builder> event) {
        final Builder builder = new Builder();
        event.accept(builder);
        return builder.build();
    }

}
