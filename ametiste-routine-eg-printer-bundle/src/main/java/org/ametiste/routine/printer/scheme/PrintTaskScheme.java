package org.ametiste.routine.printer.scheme;

import org.ametiste.routine.domain.scheme.AbstractTaskScheme;
import org.ametiste.routine.domain.scheme.TaskCreationRejectedBySchemeException;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.printer.operation.PrintOperation;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component(PrintTaskScheme.NAME)
public class PrintTaskScheme extends AbstractTaskScheme {

    public static final String NAME = "printer-eg-task";

    public static final String ALLOWED_CREATOR = "mod-backlog";

    @Override
    protected void verifyCreationRequest(Map<String, String> schemeParams, String creatorIdentifier) throws TaskCreationRejectedBySchemeException {
        if (!creatorIdentifier.equals(ALLOWED_CREATOR)) {
            throw new TaskCreationRejectedBySchemeException(
                    "Unexpected creator identifier for task scheme '" + NAME + "' expected '" + ALLOWED_CREATOR + "' but '" + creatorIdentifier + "' given.");
        }
    }

    @Override
    protected void fulfillOperations(Task task, Map<String, String> schemeParams) {

        task.addProperty(new TaskProperty("printer-eg.task.number",
                schemeParams.getOrDefault("task.number", "[none]")));

        task.addProperty(new TaskProperty("printer-eg.task.out",
                schemeParams.getOrDefault("task.out", "[none]")));

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
