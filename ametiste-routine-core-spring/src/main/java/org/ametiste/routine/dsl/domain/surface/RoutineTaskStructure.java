package org.ametiste.routine.dsl.domain.surface;

import org.ametiste.dynamics.SurfaceFeature;
import org.ametiste.dynamics.SurfaceStructure;
import org.ametiste.dynamics.Surge;
import org.ametiste.dynamics.foundation.reflection.structures.ClassStructure;
import org.ametiste.routine.dsl.annotations.TaskOperation;
import org.ametiste.routine.dsl.domain.SchemeMappingAnnotation;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 *
 * @since
 */
@SurfaceStructure
public class RoutineTaskStructure {

    private final ClassStructure<?> klass;

    private final List<? extends Surge> fieldValues;
    private final List<? extends Surge> paramValues;

    public RoutineTaskStructure(final ClassStructure<?> klass,
                                final List<? extends Surge> fieldValues,
                                final List<? extends Surge> paramValues) {
        this.klass = klass;
        this.fieldValues = fieldValues;
        this.paramValues = paramValues;
    }

    @SurfaceFeature
    public Stream<RoutineOperationStructure> operations() {
        return klass.mapMethods(
                method -> method.hasAnnotations(TaskOperation.class),
                method -> new RoutineOperationStructure(klass, method, this, fieldValues, paramValues),
                this::operationsOrder
        );
    }

    @SurfaceFeature
    public String controllerName() {
        return klass.qualifiedName();
    }

    @SurfaceFeature
    public String name() {
        return klass.annotation(SchemeMappingAnnotation::new)
                .nameOrThrow(() -> new IllegalStateException("Can't resolve task scheme name mapping."));
    }

    private int operationsOrder(final RoutineOperationStructure one, final RoutineOperationStructure another) {
        if (one.order() > another.order()) {
            return 1;
        } else if (one.order() < another.order()) {
            return -1;
        } else {
            // TODO: add scheme name to exception
            throw new IllegalStateException("Operations order is undefined. " +
                    "Please define unique operations order explicitly.");
        }
    }

}
