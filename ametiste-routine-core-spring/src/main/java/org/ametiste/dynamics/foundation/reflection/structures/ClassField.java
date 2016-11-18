package org.ametiste.dynamics.foundation.reflection.structures;

import org.ametiste.dynamics.SurfaceFeature;
import org.ametiste.dynamics.SurfaceStructure;
import org.ametiste.dynamics.foundation.elements.AnnotationSpec;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * Represents a field element of a runtime objects graph, that belongs to a class.
 *
 * @apiNote This class is structural representation of the {@link Field} class in a context
 * of the Surface Model.
 *
 * @since 1.0
 */
@SurfaceStructure(superStructure = ClassStructure.class)
public class ClassField implements Annotated {

    /**
     * A reference on the {@link Field} that represented by this structure.
     *
     * @apiNote This field is accessable for the package members
     * to provide easy access for internals. Feel free to use it inside the package.
     */
    final Field field;

    private final AnnotatedDescriptorDelegate descriptor;
    private final ClassStructure<?> classStructure;

    public ClassField(final @NotNull ClassStructure<?> classStructure, final @NotNull  Field field) {
        this.field = field;
        this.classStructure = classStructure;
        this.descriptor = new AnnotatedDescriptorDelegate(field::isAnnotationPresent, field::getAnnotation);
    }

    /**
     * Returns a type of a field.
     *
     * @return a type of a field, can't be null.
     */
    @SurfaceFeature
    @NotNull
    public Class<?> type() {
        return field.getType();
    }

    /**
     * @since 1.0
     */
    @NotNull
    @SurfaceFeature
    @Override
    public <T extends AnnotationSpec> T annotation(final @NotNull Function<AnnotatedDescriptor, T> value) {
        return value.apply(descriptor);
    }

    /**
     * @since 1.0
     */
    @SurfaceFeature
    @Override
    public boolean hasAnnotations(final @NotNull Function<AnnotatedDescriptor, ? extends AnnotationSpec> handler) {
        return descriptor.hasAnnotations(handler.apply(descriptor).annotation());
    }

}
