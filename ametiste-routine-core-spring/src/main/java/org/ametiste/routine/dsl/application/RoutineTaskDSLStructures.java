package org.ametiste.routine.dsl.application;

import org.ametiste.dynamics.SurfaceFeature;
import org.ametiste.dynamics.SurfaceStructure;
import org.ametiste.routine.dsl.annotations.RoutineTask;
import org.ametiste.routine.dsl.annotations.SchemeMapping;
import org.ametiste.routine.dsl.annotations.TaskOperation;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.function.Consumer;

import static org.ametiste.dynamics.foundation.structure.ReflectFoundation.*;

/**
 *
 * @since 1.1
 */
public class RoutineTaskDSLStructures {

    @SurfaceStructure
    public final static class RoutineDSLSurface {

        private final ClassPoolSurface enclosingSurface;

        public RoutineDSLSurface(final ClassPoolSurface enclosingSurface) {
            this.enclosingSurface = enclosingSurface;
        }

        @SurfaceFeature
        public void tasks(final Consumer<RoutineTaskSurface> consumer) {
            enclosingSurface.peekClass(
                    that -> that.hasAnnotations(RoutineTask.class, SchemeMapping.class),
                    RoutineTaskSurface::new,
                    consumer
            );
        }

    }

    @SurfaceStructure
    public static class RoutineTaskSurface {

        private final ClassSurface enclosingSurface;

        public RoutineTaskSurface(final ClassSurface enclosingSurface) {
            this.enclosingSurface = enclosingSurface;
        }

        @SurfaceFeature
        public void operations(final Consumer<RoutineOperationSurface> consumer) {
            enclosingSurface.peekMethod(
                that -> that.hasAnnotations(TaskOperation.class),
                RoutineOperationSurface::new,
                this::operationsOrder,
                consumer
            );
        }

        @SurfaceFeature
        public String name() {
            return enclosingSurface.annotationValue(SchemeMapping::schemeName, SchemeMapping.class)
                    .orElseThrow(() -> new IllegalStateException("Can't resolve task scheme name mapping."));
        }

        private int operationsOrder(final RoutineOperationSurface one, final RoutineOperationSurface another) {
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

    @SurfaceStructure
    public static class RoutineOperationSurface {

        private final MethodSurface method;

        public RoutineOperationSurface(MethodSurface method) {
            this.method = method;
        }

        @SurfaceFeature
        public String name() {
            return method.annotationValue(TaskOperation::schemeName, TaskOperation.class)
                    .orElseGet(() -> method.name());
        }

        @SurfaceFeature
        public Method method() {
            return method.instance();
        }

        @SurfaceFeature
        public int order() {
            return method.annotationValue(TaskOperation::order, TaskOperation.class)
                    .orElseThrow(() -> new IllegalStateException("Can't resolve operation name."));
        }

        @SurfaceFeature
        public Class<?> controller() {
            return method.controller();
        }

    }

}
