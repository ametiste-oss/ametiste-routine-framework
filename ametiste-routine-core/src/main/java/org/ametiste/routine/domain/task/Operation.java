package org.ametiste.routine.domain.task;

import org.ametiste.routine.domain.task.notices.Notice;
import org.ametiste.routine.domain.task.reflect.OperationFlare;

import java.util.*;

/**
 *
 * @since
 */
public class Operation {

    enum State {
       NEW, PENDING, EXECUTION, DONE, TERMINATED;
    }

    UUID id;

    String operationLabel;

    final Map<String, String> properties;

    State state;

    List<Notice> notices;

    Operation(String operationLabel, Map<String, String> properties) {
        this(UUID.randomUUID(), operationLabel, properties, State.NEW, Collections.<Notice>emptyList());
    }

    Operation(UUID id, String label, Map<String, String> properties, String state, List<Notice> notices) {
        this(id, label, properties, State.valueOf(state), notices);
    }

    Operation(UUID id, String label, Map<String, String> properties, State state, List<Notice> notices) {
        this.id = id;
        this.operationLabel = label;
        this.properties = properties;
        this.state = state;
        this.notices = new ArrayList<>(notices);
    }

    UUID id() {
        return id;
    }

    void complete() {
        state = State.DONE;
    }

    void terminate() {
        state = State.TERMINATED;
    }

    void execute() {
        state = State.EXECUTION;
    }

    void addNotice(String text) {
        this.notices.add(new Notice(text));
    }

    ExecutionLine prepareExecution() {
        state = State.PENDING;
        return new ExecutionLine(id, operationLabel, properties);
    }

    boolean isTerminated() {
        return state == State.TERMINATED;
    }

    boolean isNotDone() {
        return state == State.EXECUTION || state == State.PENDING || state == State.NEW;
    }

    public static Operation createByFlare(OperationFlare flare) {
        return new Operation(flare.flashId(), flare.flashLabel(), flare.flashProperties(),
                flare.flashState(), flare.flashNotices());
    }

}
