package org.ametiste.routine.dsl.domain;

import org.ametiste.dynamics.foundation.elements.AnnotationSpec;
import org.ametiste.dynamics.foundation.reflection.structures.AnnotatedDescriptor;
import org.ametiste.routine.dsl.annotations.SchemeMapping;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.function.Supplier;

/**
 *
 * @since
 */
public class SchemeMappingAnnotation implements AnnotationSpec {

    private final AnnotatedDescriptor annotated;

    public SchemeMappingAnnotation(final @NotNull AnnotatedDescriptor annotated) {
        this.annotated = annotated;
    }

    public String nameOrDefault(final @NotNull Supplier<String> defaultName) {
        return name().orElseGet(defaultName);
    }

    @NotNull
    public <T extends Throwable> String nameOrThrow(final @NotNull Supplier<T> exception) throws T {
        return name().orElseThrow(exception);
    }

    @NotNull
    public Optional<String> name() {
        return annotated.annotationValue(SchemeMapping::schemeName, SchemeMapping.class)
                .filter(name -> !name.equals(SchemeMapping.EMPTY_NAME));
    }

    @NotNull
    @Override
    public Class<? extends Annotation> annotation() {
        return SchemeMapping.class;
    }
}
