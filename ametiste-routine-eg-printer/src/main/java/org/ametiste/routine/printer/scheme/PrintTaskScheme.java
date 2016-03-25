package org.ametiste.routine.printer.scheme;

import org.ametiste.routine.domain.scheme.*;
import org.ametiste.routine.domain.task.properties.TaskProperty;
import org.ametiste.routine.mod.backlog.mod.ModBacklog;
import org.ametiste.routine.printer.operation.PrintOperation;
import org.ametiste.routine.printer.operation.PrintOperationScheme;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static org.ametiste.routine.infrastructure.persistency.jpa.data.OperationData_.task;

@Component
public class PrintTaskScheme extends AbstractTaskScheme<PrintTaskSchemeParams> {

    public static final String NAME = "printer-eg-task";

    public static final String ALLOWED_CREATOR = ModBacklog.MOD_ID;

    public PrintTaskScheme() {
        super(NAME, PrintTaskSchemeParams::new);
    }

    @Override
    protected void verifyCreationRequest(PrintTaskSchemeParams schemeParams, String creatorIdentifier) throws TaskCreationRejectedBySchemeException {
        if (!creatorIdentifier.equals(ALLOWED_CREATOR)) {
            throw new TaskCreationRejectedBySchemeException(
                    "Unexpected creator identifier for task scheme '" + NAME + "' expected '" + ALLOWED_CREATOR + "' but '" + creatorIdentifier + "' given.");
        }
    }

    @Override
    protected void fulfillProperties(final TaskPropertiesReceiver task, final PrintTaskSchemeParams schemeParams) {
        task.addProperty("printer-eg.task.number", schemeParams.taskNumber());
        task.addProperty("printer-eg.task.out", schemeParams.taskOut());
    }

    @Override
    protected void fulfillOperations(final TaskOperationInstaller task, PrintTaskSchemeParams schemeParams) {
        task.addOperation(PrintOperationScheme.class, params -> {
                params.printOut(schemeParams.taskOut()+"::operation-1");
            }
        );
        task.addOperation(PrintOperationScheme.class, params -> {
                params.printOut(schemeParams.taskOut()+"::operation-2");
            }
        );
        task.addOperation(PrintOperationScheme.class, params -> {
                params.printOut(schemeParams.taskOut()+"::operation-3");
            }
        );
    }
}
