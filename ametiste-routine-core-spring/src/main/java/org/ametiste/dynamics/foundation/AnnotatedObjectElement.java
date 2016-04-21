package org.ametiste.dynamics.foundation;

import org.ametiste.dynamics.SurfaceElement;
import org.ametiste.dynamics.DynamicSurfaceStructure;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;
import java.util.function.Function;

public abstract class AnnotatedObjectElement<T extends AnnotatedElement>
        implements SurfaceElement {

    protected final T element;
    private final GeneralDelegate delegate;

    public AnnotatedObjectElement(final T element,
                                  final GeneralDelegate delegate) {
        this.element = element;
        this.delegate = delegate;
    }

    @Override
    public boolean isAnnotatedBy(final Class<? extends Annotation> annotation) {
        return element.isAnnotationPresent(annotation);
    }

    @Override
    public <S extends Annotation, T> T annotationValue(final Class<S> annotation, final Function<S, T> valueProvider) {
        return valueProvider.apply(element.getDeclaredAnnotation(annotation));
    }

    @Override
    public <T> Optional<T> mapName(final Function<String, T> transform) {
        return delegate.mapName(transform);
    }

    @Override
    public boolean hasStructureOf(final DynamicSurfaceStructure structure) {
        return delegate.actsLike(structure);
    }

    @Override
    public <F, T> Optional<T> mapFeature(final Function<F, T> transform, final Class<F> feature) {
        return delegate.map(transform, feature);
    }

}