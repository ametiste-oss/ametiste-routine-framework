package org.ametiste.routine.dsl.domain.surface;

import org.ametiste.dynamics.SurfaceFeature;
import org.ametiste.dynamics.SurfaceStructure;
import org.ametiste.dynamics.Surge;
import org.ametiste.dynamics.foundation.reflection.structures.ClassMethod;
import org.ametiste.dynamics.foundation.reflection.structures.ClassStructure;
import org.ametiste.laplatform.protocol.ProtocolGateway;
import org.ametiste.routine.dsl.domain.SchemeMappingAnnotation;
import org.ametiste.routine.dsl.domain.TaskOperationAnnotation;

import java.util.List;

/**
 *
 * @since
 */
@SurfaceStructure
public class RoutineOperationStructure {

    private final ClassStructure<?> controller;
    private final ClassMethod action;

    private final RoutineTaskStructure task;
    private final List<? extends Surge> fields;
    private final List<? extends Surge> params;

    public RoutineOperationStructure(final ClassStructure<?> controller,
                                     final ClassMethod action,
                                     final RoutineTaskStructure task,
                                     final List<? extends Surge> fields,
                                     final List<? extends Surge> params) {
        this.controller = controller;
        this.action = action;
        this.task = task;
        this.fields = fields;
        this.params = params;
    }

    @SurfaceFeature
    public String actionName() {
        return action.qualifiedName();
    }

    @SurfaceFeature
    public String name() {
        return task.name() + "-" + action.annotation(SchemeMappingAnnotation::new)
                .nameOrDefault(() -> action.name());
    }

    @SurfaceFeature
    public void invoke(final ProtocolGateway gateway) {
        controller.newInstance(fields, gateway, controllerInstance ->
            controllerInstance.method(action, instanceMethod ->
                instanceMethod.invoke(params, gateway)
            )
        );
    }

    @SurfaceFeature
    public int order() {
        return action.annotation(TaskOperationAnnotation::new)
                .orderOrThrow(() -> new IllegalStateException("Can't resolve operation name."));
    }

}
