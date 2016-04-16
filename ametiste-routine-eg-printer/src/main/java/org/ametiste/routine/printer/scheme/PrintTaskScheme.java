package org.ametiste.routine.printer.scheme;

import org.ametiste.routine.domain.scheme.*;
import org.ametiste.routine.mod.backlog.mod.ModBacklog;
import org.ametiste.routine.printer.operation.PrintOperationScheme;
import org.springframework.stereotype.Component;

@Component
public class PrintTaskScheme extends AbstractTaskScheme<PrintTaskSchemeParams> {

    public static final String NAME = "printer-eg-task";

    public static final String ALLOWED_CREATOR = ModBacklog.MOD_ID;

    public PrintTaskScheme() {
        super(NAME, PrintTaskSchemeParams::new);
    }

    @Override
    protected void verifyCreationRequest(final PrintTaskSchemeParams schemeParams, final String creatorIdentifier) throws TaskCreationRejectedBySchemeException {
        if (!creatorIdentifier.startsWith(ALLOWED_CREATOR)) {
            throw new TaskCreationRejectedBySchemeException(
                    "Unexpected creator identifier for task scheme '" + NAME + "' expected '" + ALLOWED_CREATOR + "' but '" + creatorIdentifier + "' given.");
        }
    }

    @Override
    protected void fulfillProperties(final TaskPropertiesReceiver task, final PrintTaskSchemeParams schemeParams) {
        task.addProperty("printer-eg.task.number", Integer.toString(schemeParams.taskNumber()));
        task.addProperty("printer-eg.task.out", schemeParams.taskMessage());
    }

    @Override
    protected void fulfillOperations(final TaskOperationReceiver task, final PrintTaskSchemeParams schemeParams) {
        task.addOperation(PrintOperationScheme.class, params -> {
                params.printOut(schemeParams.taskMessage()+"::operation-1");
                params.delayTime(schemeParams.delayTime());
            }
        );
    }
}
