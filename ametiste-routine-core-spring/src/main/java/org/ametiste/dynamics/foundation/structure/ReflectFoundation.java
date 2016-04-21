package org.ametiste.dynamics.foundation.structure;

import org.ametiste.dynamics.DynamicSurfaceStructure;
import org.ametiste.dynamics.SurfaceFeature;
import org.ametiste.dynamics.SurfaceStructure;
import org.ametiste.lang.BiLet;
import org.ametiste.lang.BiTransformable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 *
 * @since 1.1
 */
public final class ReflectFoundation {

    private ReflectFoundation() { }

    @SurfaceStructure
    public static final DynamicSurfaceStructure referenceTo(Class<?> type) {
        return null;
//        return surface -> surface.satisfiedBy(f -> f.referencesTo(type), ReferenceFeature.class);
    }

    public static class RuntimePattern implements
            BiLet<Class<? extends Annotation>, Class<? extends Annotation>[]>,
            BiTransformable<Class<? extends Annotation>, Class<? extends Annotation>[]> {

        public static final RuntimePattern EMPTY = new RuntimePattern();

        private final Class<? extends Annotation> annotation;
        private final Class<? extends Annotation>[] annotations;

        private RuntimePattern(final Class<? extends Annotation> annotation,
                               final Class<? extends Annotation>[] annotations) {
            this.annotation = annotation;
            this.annotations = annotations;
        }

        private RuntimePattern() {
            this(null, new Class[] {});
        }

        public static final RuntimePattern over(UnaryOperator<RuntimePattern> matcher) {
            return matcher.apply(EMPTY);
        }

        /**
         * Add the given annotations to a runtime pattern.
         *
         * @param annotation at least one annotation is required,
         *                   so there is one reuired parameter that must be not null
         * @param annotations set of additional annotations, may be empty, but elements must be not null
         *
         * @return new runtime pattern that match object which has all annotations
         * from the given annotations set
         */
        public RuntimePattern hasAnnotations(final Class<? extends Annotation> annotation,
                                             final Class<? extends Annotation>... annotations) {
            return new RuntimePattern(annotation, annotations);
        }

        @Override
        public void let(final BiConsumer<Class<? extends Annotation>, Class<? extends Annotation>[]> consumer) {
            BiLet.let(annotation, annotations, consumer);
        }

        @Override
        public <R> R map(final BiFunction<Class<? extends Annotation>, Class<? extends Annotation>[], R> transformation) {
            return transformation.apply(annotation, annotations);
        }

    }

    public interface RuntimePreSurface {

        void findClasses(UnaryOperator<RuntimePattern> matcher, Consumer<Class<?>> consumer);

        static Supplier<RuntimePreSurface> definedBy(final RuntimePreSurface preSurface) {
            return () -> preSurface;
        }

    }

    @SurfaceStructure
    public static final class ClassPoolSurface {

        private final RuntimePreSurface preSurface;

        public ClassPoolSurface(final RuntimePreSurface preSurface) {
            this.preSurface = preSurface;
        }

        @SurfaceFeature
        public <T> void peekClass(final UnaryOperator<RuntimePattern> pattern,
                                     final Function<ClassSurface, T> mapper,
                                     final Consumer<T> consumer) {
            preSurface.findClasses(pattern,
                c -> consumer.accept(mapper.apply(new ClassSurface(c)))
            );
        }

    }

    @SurfaceStructure
    public static class ClassSurface {

        private final Class<?> klass;

        public ClassSurface(Class<?> klass) {
            this.klass = klass;
        }

        @SurfaceFeature
        public boolean hasAnnotations(Class<? extends Annotation> ...annotations) {
            return Stream.of(annotations).allMatch(a -> klass.isAnnotationPresent(a));
        }

        @SurfaceFeature
        public <T> void peekMethod(final UnaryOperator<RuntimePattern> matcher,
                                   final Function<MethodSurface, T> mapper,
                                   final Comparator<T> order,
                                   final Consumer<T> consumer) {
            peekMethod(matcher, mapper, s -> s.sorted(order), consumer);
        }

        @SurfaceFeature
        public <T> void peekMethod(final UnaryOperator<RuntimePattern> matcher,
                                   final Function<MethodSurface, T> mapper,
                                   final Consumer<T> consumer) {
            peekMethod(matcher, mapper, s -> s, consumer);
        }

        @SurfaceFeature
        public <T> void peekMethod(final UnaryOperator<RuntimePattern> matcher,
                                   final Function<MethodSurface, T> mapper,
                                   final UnaryOperator<Stream<T>> enhance,
                                   final Consumer<T> consumer) {
            RuntimePattern.over(matcher).let((annotation, annotations) ->
                    enhance.apply(Stream.of(klass.getDeclaredMethods())
                            .filter(m -> Stream.of(annotations).allMatch(m::isAnnotationPresent))
                            .map(m -> new MethodSurface(this, m))
                            .map(mapper)
                    ).forEach(consumer)
            );
        }

        @SurfaceFeature
        public Class<?> type() {
            return klass;
        }

        @SurfaceFeature
        public <T, A extends Annotation> Optional<T> annotationValue(final Function<A, T> value, final Class<A> annotation) {
            if (klass.isAnnotationPresent(annotation)) {
                return Optional.ofNullable(value.apply(klass.getDeclaredAnnotation(annotation)));
            } else {
                return Optional.empty();
            }
        }

    }

    @SurfaceStructure
    public static class MethodSurface {

        private final Method method;
        private final ClassSurface enclosingSurface;

        public MethodSurface(final ClassSurface enclosingSurface, final Method method) {
            this.enclosingSurface = enclosingSurface;
            this.method = method;
        }

        @SurfaceFeature
        public Class<?> controller() {
            return enclosingSurface.type();
        }

        @SurfaceFeature
        public String name() {
            return method.getName();
        }

        @SurfaceFeature
        public <T, A extends Annotation> Optional<T> annotationValue(final Function<A, T> value, final Class<A> annotation) {
            if (method.isAnnotationPresent(annotation)) {
                return Optional.ofNullable(value.apply(method.getDeclaredAnnotation(annotation)));
            } else {
                return Optional.empty();
            }
        }

        public Method instance() {
            return method;
        }

    }

}
