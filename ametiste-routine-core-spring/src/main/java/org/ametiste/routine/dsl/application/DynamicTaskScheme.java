package org.ametiste.routine.dsl.application;

import org.ametiste.routine.domain.scheme.OperationScheme;
import org.ametiste.routine.domain.scheme.TaskBuilder;
import org.ametiste.routine.domain.scheme.TaskScheme;
import org.ametiste.routine.domain.scheme.TaskSchemeException;
import org.ametiste.routine.dsl.infrastructure.protocol.DirectDynamicParamsProtocol;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @since
 */
public class DynamicTaskScheme implements TaskScheme<DynamicParamsProtocol> {

    private final String name;

    private final List<DynamicOperationScheme> operations;

    public DynamicTaskScheme(final String name, final List<DynamicOperationScheme> operations) {
        this.name = name;
        this.operations = new ArrayList<>(operations);
    }

    @Override
    public String schemeName() {
        return name;
    }

    @Override
    public void setupTask(final TaskBuilder<DynamicParamsProtocol> taskBuilder,
                          final Consumer<DynamicParamsProtocol> paramsInstaller,
                          final String creatorIdenifier) throws TaskSchemeException {

        final DynamicParamsProtocol dynamicParamsProtocol = new DirectDynamicParamsProtocol();
        paramsInstaller.accept(dynamicParamsProtocol);
        // NOTE: operations must be sorted in a "valid" order, so just adding em one after one
        operations.forEach(
                op -> taskBuilder.addOperation(op.schemeName(), dynamicParamsProtocol)
        );

    }
}
