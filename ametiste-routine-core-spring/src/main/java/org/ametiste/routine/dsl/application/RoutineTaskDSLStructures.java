package org.ametiste.routine.dsl.application;

import org.ametiste.dynamics.SurfaceFeature;
import org.ametiste.dynamics.SurfaceStructure;
import org.ametiste.dynamics.foundation.BaseSurface;
import org.ametiste.routine.dsl.annotations.SchemeMapping;
import org.ametiste.routine.dsl.annotations.TaskOperation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;


/**
 *
 * @since 1.1
 */
public class RoutineTaskDSLStructures {

    public static final class RuntimeSurface {

        public RuntimeSurface() {

        }

    }

    @SurfaceStructure
    public static final class ClassPoolSurface {

        private final RuntimeSurface featuredSurface;

        public ClassPoolSurface(final RuntimeSurface featuredSurface) {
            this.featuredSurface = featuredSurface;
        }

        @SurfaceFeature
        public <F, T> void peekClass(final Predicate<ClassSurface> predicate,
                                  // NOTE: функция-обертка просто чтоб писать depictedAs -> BlahBlah::new
                                  final Function<ClassSurface, T> mapper,
                                  final BiConsumer<ClassSurface, T> consumer) {

        }

    }

    @SurfaceStructure
    public static class ClassSurface {

        private final Class<?> klass;
        private final ClassPoolSurface featuredSurface;

        public ClassSurface(final ClassPoolSurface featuredSurface, Class<?> klass) {
            this.featuredSurface = featuredSurface;
            this.klass = klass;
        }

        @SurfaceFeature
        public boolean hasAnnotations(Class<? extends Annotation> ...annotations) {
            return Stream.of(annotations).allMatch(a -> klass.isAnnotationPresent(a));
        }

        @SurfaceFeature
        public Class<?> type() {
            return klass;
        }

    }

    public final static class RoutineDSLSurface {

        private final ClassPoolSurface enclosing;

        public RoutineDSLSurface(final ClassPoolSurface enclosing) {
            this.enclosing = enclosing;
        }

        @SurfaceFeature
        public void tasks(final Consumer<RoutineTaskSurface> consumer) {
//            enclosingSurface.eachClassSurface(c -> c.hasAnnotations(RoutineTask.class, SchemeMapping.class), classSurface ->
//                consumer.accept(new RoutineTaskSurface(classSurface))
//            );
        }

        public boolean test(final ClassPoolSurface enclosing) {
            return enclosing.hasClassesAnnotatedBy();
        }

    }

    @SurfaceStructure
    public static class RoutineTaskSurface {

        public RoutineTaskSurface(ClassSurface dslSurface) {

        }

        @SurfaceFeature
        public void operations(final Consumer<RoutineOperationSurface> consumer) {
            consumer.accept(null);
        }

        @SurfaceFeature
        public String name() {
            return enclosingSurface.map(SchemeMapping::schemeName, SchemeMapping.class).orElseThrow(()
                    -> new IllegalStateException("Can't resolve task scheme name mapping.")
            );
        }

    }

    @SurfaceStructure
    public static class RoutineOperationSurface {

        public RoutineOperationSurface(RoutineTaskSurface operationSurface) {

        }

        @SurfaceFeature
        public String name() {
            return enclosingSurface.map(TaskOperation::schemeName, TaskOperation.class).orElseGet(()
                    -> enclosingSurface.map(Method::getName, Method.class).orElseThrow(()
                    -> new IllegalStateException("Can't resolve operation name."))
            );
        }

        @SurfaceFeature
        public Method method() {
            return enclosingSurface.map(m -> m, Method.class).orElseThrow(()
                    -> new IllegalStateException("Can't resolve operation method.")
            );
        }

        @SurfaceFeature
        public int order() {
            return enclosingSurface.map(TaskOperation::order, TaskOperation.class).orElseThrow(()
                    -> new IllegalStateException("Can't resolve operation name.")
            );
        }

        @SurfaceFeature
        public Class<?> type() {
            return null;
        }

    }

}
