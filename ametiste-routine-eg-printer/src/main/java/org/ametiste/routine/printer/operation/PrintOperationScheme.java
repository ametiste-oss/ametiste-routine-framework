package org.ametiste.routine.printer.operation;

import org.ametiste.routine.domain.scheme.AbstractOperationScheme;
import org.ametiste.routine.domain.scheme.OperationScheme;
import org.ametiste.routine.domain.scheme.TaskBuilder;
import org.ametiste.routine.domain.task.Task;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 * @since
 */
@Component
public class PrintOperationScheme extends AbstractOperationScheme<PrintOperationParams> {

    public static final String NAME = "print-operation";

    public PrintOperationScheme() {
        super(NAME, PrintOperationParams::new);
    }

}
