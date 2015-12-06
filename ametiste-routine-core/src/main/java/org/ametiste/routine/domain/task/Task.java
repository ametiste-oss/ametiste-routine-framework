package org.ametiste.routine.domain.task;

import org.ametiste.domain.DomainStateReflector;
import org.ametiste.routine.domain.task.notices.Notice;
import org.ametiste.routine.domain.task.properties.BasicTaskProperty;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.domain.task.reflect.OperationFlare;
import org.ametiste.routine.domain.task.reflect.TaskLens;
import org.ametiste.routine.domain.task.reflect.TaskReflection;

import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 *
 * @since
 */
public class Task implements DomainStateReflector<TaskReflection> {

    public static String SCHEME_PROPERTY_NAME = "task.scheme";

    public static String CREATOR_PROPERTY_NAME = "created.by";

    public enum State {

        NEW {
            @Override
            void canCompleteOperation() {
                throw new IllegalStateException("Task new and operation can't be completed.");
            }

            @Override
            void canUpdateOperation() {
                throw new IllegalStateException("Task new and operations can't be updated yet.");
            }
        },

        EXECUTION {

            @Override
            void canUpdateOperation() {
                // NOTE: operations can be updated only during execution or pending phase.
            }

            @Override
            void canBeExecuted() {
                throw new IllegalStateException("Task already executing.");
            }

            @Override
            void canBeModified() {
                throw new IllegalStateException("Task already executing and can't be modified.");
            }

        },

        PENDING {

            @Override
            void canBeModified() {
                throw new IllegalStateException("Task already pending and can't be modified.");
            }

            @Override
            void canUpdateOperation() {
                // NOTE: operations can be updated only during execution or pending phase.
            }

            @Override
            void canBeExecuted() {
                throw new IllegalStateException("Task already executing.");
            }

        },

        TERMINATED {

            @Override
            void canBeExecuted() {
                throw new IllegalStateException("Task already done.");
            }

            @Override
            void canBeModified() {
                throw new IllegalStateException("Task already done and can't be modified.");
            }

            @Override
            void canCompleteOperation() {
                throw new IllegalStateException("Task already done and operation can't be completed.");
            }

            @Override
            void canUpdateOperation() {
                throw new IllegalStateException("Task already done.");
            }

            @Override
            void canBeCompleted() {
                throw new IllegalStateException("Task already done.");
            }

            @Override
            void canBeFinal() { }

        },

        DONE {

            @Override
            void canBeExecuted() {
                throw new IllegalStateException("Task already done.");
            }

            @Override
            void canUpdateOperation() {
                throw new IllegalStateException("Task already done.");
            }

            @Override
            void canBeModified() {
                throw new IllegalStateException("Task already done and can't be modified.");
            }

            @Override
            void canCompleteOperation() {
                throw new IllegalStateException("Task already done and operation can't be completed.");
            }

            @Override
            void canBeCompleted() {
                throw new IllegalStateException("Task already done.");
            }

            @Override
            void canBeFinal() { }

        };

        /**
         * <p>
         *   Defines 'active' superstate as composition of
         *   states in which task may be treated as 'active', it means
         *   that task not done and can be or already executing.
         * </p>
         * */
        @Deprecated
        public static State[] activeStates = { NEW, PENDING, EXECUTION };

        public static List<State> activeStatesList = Arrays.asList(activeStates);

        /**
         * Defines can be the task executed in the given state.
         */
        void canBeExecuted() { };

        /**
         * Defines can be the task modified in the given state.
         */
        void canBeModified() { };

        /**
         * Defines can be the task completed in the given state.
         */
        void canBeCompleted() { };

        /**
         * Defines can be the task's operations completed in the given state.
         */
        void canCompleteOperation() { };

        /**
         * Defines can be the task's operations updated in the given state.
         */
        void canUpdateOperation() { };

        /**
         * Defines can be the state used as final state of the task.
         */
        void canBeFinal() {
            throw new IllegalStateException("State '" + name() + "' can't be a final state.");
        };

    }

    private class ReflectedTask implements TaskReflection {

        // TODO: если тут сделать проверку, что каждый из методов был вызван, то можно контролировать
        // реализацию отрожения и быть уверенным, что не забыли ни чего отобразить
        // но выглядеть это будет как гавно, нужен какой-то мета-код :)

        @Override
        public void flareTaskId(UUID taskId) {
            id = taskId;
        }

        @Override
        public void flareTaskState(State flareState) {
            inState = flareState;
        }

        @Override
        public void flareOperation(OperationFlare operationFlare) {
            final Operation flared = Operation.createByFlare(operationFlare);
            Task. this.operationsOrder.add(flared);
            Task. this.operations.put(operationFlare.flashId(), flared);
        }

        @Override
        public void flareProperty(String name, String value) {
            Task. this.properties.put(name, new BasicTaskProperty(name, value));
        }

        @Override
        public void flareTaskTimes(Instant creationTime, Instant executionStartTime, Instant completionTime) {
            Task. this.creationTime = creationTime;
            Task. this.executionStartTime = executionStartTime;
            Task. this.completionTime = completionTime;
        }

        @Override
        public void flareNotice(Notice notice) {
            Task. this.notices.add(notice);
        }

        @Override
        public void reflect(TaskReflection reflection) {

            reflection.flareTaskId(id);
            reflection.flareTaskState(inState);

            reflection.flareTaskTimes(creationTime, executionStartTime, completionTime);

            notices.forEach(reflection::flareNotice);

            operationsOrder.forEach((x) -> {
                reflection.flareOperation(
                    new OperationFlare(x.id, x.operationLabel, x.properties, x.state.name(), x.notices));
            });

            properties.values().forEach((p) -> {
                reflection.flareProperty(p.name(), p.value());
            });

        }

    }

    private State inState;

    private UUID id;

    private Instant creationTime;

    private Instant executionStartTime;

    private Instant completionTime;

    private final List<Notice> notices = new ArrayList<>();

    private final Map<UUID, Operation> operations = new HashMap<>();

    /*
        Hijack solution to save operation order, don't want to use ordered map yet.
     */
    private final ArrayList<Operation> operationsOrder = new ArrayList<>();

    private final Map<String, TaskProperty> properties = new HashMap<>();

    private final TaskReflection reflection = this. new ReflectedTask();

    public Task() {
        this.id = UUID.randomUUID();
        this.inState = State.NEW;
        this.creationTime = Instant.now();
    }

    public Task(TaskReflection reflection) {
        reflection.reflect(this.reflection);
    }

    public UUID entityId() {
        return id;
    }

    public void addOperation(String operationLabel, Map<String, String> properties) {

        inState.canBeModified();

        final Operation operation = new Operation(operationLabel, properties);

        operations.put(operation.id, operation);
        operationsOrder.add(operation);
    }

    public void addProperty(TaskProperty property) {
        properties.put(property.name(), property);
    }

    public boolean hasProperty(TaskProperty property) {

        final TaskProperty taskProperty = properties.get(property.name());

        return taskProperty == null && taskProperty.getClass().isAssignableFrom(property.getClass()) ?
                false : taskProperty.equalsTo(property);
    }

    public ExecutionOrder prepareExecution() {

        prepareTaskExecution();

        final List<ExecutionLine> orderLines = operationsOrder.stream()
                .map(Operation::prepareExecution)
                .collect(Collectors.toList());

        return new ExecutionOrder(id, orderLines);

    }

    public void noticeOperation(UUID operationId, String message) {
        invokeOnOperation(operationId, o -> o.addNotice(message));
    }

    public void completeOperation(UUID operationId) {
        inState.canCompleteOperation();
        invokeOnOperation(operationId, Operation::complete);
        checkIsTaskCompleted();
    }

    public void terminateOperation(UUID operationId) {
        inState.canCompleteOperation();
        invokeOnOperation(operationId, Operation::terminate);
        checkIsTaskCompleted();
    }

    public void executeOperation(UUID operationId) {

        inState.canUpdateOperation();
        invokeOnOperation(operationId, Operation::execute);

        // NOTE: after first operation execution started, the task is moved to execution state
        if (inState != State.EXECUTION) {
            inState = State.EXECUTION;
        }
    }

    public List<UUID> terminate(String message) {

        completeTask(State.TERMINATED);

        notices.add(new Notice(message));

        final List<Operation> toBeterminated = operations.values().stream()
            .filter((o) -> {
                return o.state == Operation.State.EXECUTION;
            }).collect(Collectors.toList());

        toBeterminated.forEach((o) -> {
            o.addNotice("Terminated with reason : " + message);
            o.terminate();
        });

        final List<UUID> terminated = toBeterminated.stream()
            .map((o) -> {
                return o.id;
            }).collect(Collectors.toList());

        return terminated;
    }

    @Override
    public void reflectAs(TaskReflection domainReflection) {
        this.reflection.reflect(domainReflection);
    }

    public void flareTo(Supplier<TaskLens> taskLensSupplier) {
        taskLensSupplier.get();
    }

    private void invokeOnOperation(UUID operationId, Consumer<Operation> operationConsumer) {

        final Operation operation;

        if (operations.containsKey(operationId)) {
            operationConsumer.accept(operations.get(operationId));
        } else {
            throw new IllegalArgumentException(
                    "Task does not own operation with the given id: " + operationId.toString()
            );
        }
    }

    /**
     * Checks if task is complete, if all operations are completed - task is completed.
     *
     * Note, if any operation has TERMINATED state, the task would be marked as terminated.
     *
     */
    private void checkIsTaskCompleted() {

        for (Operation op : operations.values()) {
            if (op.isNotDone()) {
                return;
            }
        }

        if (operations.values().stream().filter(Operation::isTerminated).findFirst().isPresent()) {
            completeTask(State.TERMINATED);
        } else {
            completeTask(State.DONE);
        }

    }

    private void prepareTaskExecution() {
        inState.canBeExecuted();
        inState = State.PENDING;
        executionStartTime = Instant.now();
    }


    /**
     * Completes task with the given state, note the state must be able to be used as final state.
     *
     * @param state final task state
     */
    private void completeTask(State state) {
        state.canBeFinal();
        inState.canBeCompleted();

        completionTime = Instant.now();
        inState = state;
    }
}
