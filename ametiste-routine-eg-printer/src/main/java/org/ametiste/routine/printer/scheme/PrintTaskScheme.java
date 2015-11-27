package org.ametiste.routine.printer.scheme;

import org.ametiste.routine.domain.scheme.AbstractTaskScheme;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.printer.operation.PrintOperation;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component(PrintTaskScheme.NAME)
public class PrintTaskScheme extends AbstractTaskScheme {

    public static final String NAME = "printer-eg-task";

    @Override
    protected void fulfillOperations(Task task, Map<String, String> schemeParams) {
        task.addOperation(PrintOperation.NAME,
                Collections.singletonMap("out", schemeParams.getOrDefault("out", "[none]")+"::operation-1"));
        task.addOperation(PrintOperation.NAME,
                Collections.singletonMap("out", schemeParams.getOrDefault("out", "[none]")+"::operation-2"));
        task.addOperation(PrintOperation.NAME,
                Collections.singletonMap("out", schemeParams.getOrDefault("out", "[none]")+"::operation-3"));
        task.addOperation(PrintOperation.NAME,
                Collections.singletonMap("out", schemeParams.getOrDefault("out", "[none]")+"::operation-4"));
    }
}
