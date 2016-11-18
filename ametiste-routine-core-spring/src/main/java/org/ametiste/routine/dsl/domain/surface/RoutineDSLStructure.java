package org.ametiste.routine.dsl.domain.surface;

import org.ametiste.dynamics.SurfaceFeature;
import org.ametiste.dynamics.SurfaceStructure;
import org.ametiste.dynamics.foundation.elements.AnnotatedRefProcessor;
import org.ametiste.dynamics.foundation.elements.ReflectionSurfaceBinding;
import org.ametiste.dynamics.foundation.reflection.structures.ClassPool;
import org.ametiste.routine.dsl.annotations.RoutineTask;
import org.ametiste.routine.dsl.annotations.SchemeMapping;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *
 * @since
 */
@SurfaceStructure
public final class RoutineDSLStructure {

    private final ClassPool classPool;

    private final List<ReflectionSurfaceBinding> fieldValues;
    private final List<ReflectionSurfaceBinding> paramValues;

    public RoutineDSLStructure(final ClassPool classPool,
                               final List<AnnotatedRefProcessor> fieldValues,
                               final List<AnnotatedRefProcessor> paramValues) {

        this.classPool = classPool;

        this.fieldValues = fieldValues.stream().map(ReflectionSurfaceBinding::new)
                .collect(Collectors.toList());

        this.paramValues = paramValues.stream().map(ReflectionSurfaceBinding::new)
                .collect(Collectors.toList());

    }

    @SurfaceFeature
    public void tasks(final Consumer<RoutineTaskStructure> consumer) {
        classPool.classes(
                klass -> klass.hasAnnotations(RoutineTask.class, SchemeMapping.class),
                klass -> new RoutineTaskStructure(klass, fieldValues, paramValues),
                consumer
        );
    }

}
