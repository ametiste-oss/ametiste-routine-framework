package org.ametiste.routine.meta.scheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TaskSchemeTrace {

    private final String schemeName;
    private final List<TaskSchemeCall> calls;

    TaskSchemeTrace(String schemeName, List<TaskSchemeCall> calls) {
        this.schemeName = schemeName;
        this.calls = new ArrayList<>(calls);
    }

    public String name() {
        return schemeName;
    }

    public void calls(Consumer<TaskSchemeCall> schemeCallConsumer) {
        calls.stream().forEach(schemeCallConsumer);
    }

}
