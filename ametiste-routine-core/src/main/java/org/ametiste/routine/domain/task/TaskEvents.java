package org.ametiste.routine.domain.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 *
 * @since
 */
public class TaskEvents {

    private final List<Consumer<TaskEvents>> consumeOrder;

    private final List<Consumer<TaskDoneEvent>> doneConsumers;
    private final List<Consumer<TaskTerminatedEvent>> terminatedConsumers;
    private final List<Consumer<OperationTerminatedEvent>> opTerminatedConsumers;

    private TaskEvents(List<Consumer<TaskEvents>> consumeOrder) {
        this.consumeOrder = consumeOrder;
        this.doneConsumers = new ArrayList<>();
        this.terminatedConsumers = new ArrayList<>();
        this.opTerminatedConsumers = new ArrayList<>();
    }

    public TaskEvents taskDone(Consumer<TaskDoneEvent> taskDoneEvent) {
        doneConsumers.add(taskDoneEvent);
        return this;
    }

    public TaskEvents taskTerminated(Consumer<TaskTerminatedEvent> taskTerminatedEvent) {
        terminatedConsumers.add(taskTerminatedEvent);
        return this;
    }

    public TaskEvents operationTerminated(Consumer<OperationTerminatedEvent> operationTerminatedEvent) {
        opTerminatedConsumers.add(operationTerminatedEvent);
        return this;
    }

    public void consume() {
        consumeOrder.forEach(c -> c.accept(this));
    }

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
