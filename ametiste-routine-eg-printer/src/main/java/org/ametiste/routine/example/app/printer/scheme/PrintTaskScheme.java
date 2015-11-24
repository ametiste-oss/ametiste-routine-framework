package org.ametiste.routine.example.app.printer.scheme;

import org.ametiste.routine.domain.scheme.AbstractTaskScheme;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.properties.BasicTaskProperty;
import org.ametiste.routine.example.app.printer.operation.PrintOperation;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component(PrintTaskScheme.NAME)
public class PrintTaskScheme extends AbstractTaskScheme {

    public static final String NAME = "printer-eg-task";

    @Override
    protected void fulfillOperations(Task task, Map<String, String> schemeParams) {

        task.addProperty(new BasicTaskProperty("test-property", "test-value"));

        task.addOperation(PrintOperation.NAME,
                Collections.singletonMap("out", schemeParams.getOrDefault("out", "[none]")+"1"));
        task.addOperation(PrintOperation.NAME,
                Collections.singletonMap("out", schemeParams.getOrDefault("out", "[none]")+"2"));
        task.addOperation(PrintOperation.NAME,
                Collections.singletonMap("out", schemeParams.getOrDefault("out", "[none]")+"3"));
        task.addOperation(PrintOperation.NAME,
                Collections.singletonMap("out", schemeParams.getOrDefault("out", "[none]")+"4"));
    }
}
