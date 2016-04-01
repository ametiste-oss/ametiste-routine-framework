package org.ametiste.routine.printer.operation;

import org.ametiste.routine.domain.scheme.AbstractOperationScheme;
import org.ametiste.routine.domain.scheme.OperationScheme;
import org.ametiste.routine.domain.scheme.StatelessOperationScheme;
import org.ametiste.routine.domain.scheme.TaskBuilder;
import org.ametiste.routine.domain.task.Task;
import org.ametiste.routine.sdk.operation.OperationExecutor;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 * @since
 */
//@Component
public class PrintOperationScheme extends StatelessOperationScheme<PrintOperationParams> {

    public static final String NAME = "print-operation";

    public PrintOperationScheme() {
        super(NAME, PrintOperationParams::new, PrintExecutor::new);
    }

}
